package edu.sdccd.cisc191.client.service;

import edu.sdccd.cisc191.client.dto.*;
import edu.sdccd.cisc191.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class GameGrpcClient {

    private final ManagedChannel channel;
    private final GameServiceGrpc.GameServiceBlockingStub blockingStub;

    public GameGrpcClient(
            @Value("${game.grpc.host:localhost}") String host,
            @Value("${game.grpc.port:50051}") int port
    ) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        this.blockingStub = GameServiceGrpc.newBlockingStub(channel);
    }

    public SseEmitter joinMatch(JoinMatchWebRequest webRequest) {
        SseEmitter emitter = new SseEmitter(0L);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                JoinMatchRequest request = JoinMatchRequest.newBuilder()
                    .setPlayerName(safe(webRequest.playerName(), "Player"))
                    .setDifficulty(safe(webRequest.difficulty(), "Normal"))
                    .setRanked(webRequest.ranked())
                    .setBotOpponent(webRequest.botOpponent())
                    .build();

                Iterator<MatchStateUpdate> responseIterator = blockingStub.joinMatch(request);

                while (responseIterator.hasNext()) {
                    MatchStateUpdate response = responseIterator.next();

                    MatchUpdateWebPacket update = new MatchUpdateWebPacket(
                        response.getMatchId(),
                        response.getStatus().name(),
                        response.getIsPlayerTurn(),
                        MatchUpdateWebPacket.Player.from(response.getPlayer()),
                        MatchUpdateWebPacket.Player.from(response.getOpponent()),
                        response.getTerrainList(),
                        MatchUpdateWebPacket.Properties.from(response.getProperties())
                    );

                    emitter.send(SseEmitter.event().name("match-update").data(update));
                }
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            } finally {
                executor.shutdown();
            }
        });

        return emitter;
    }

    public void playerTurn(PlayerTurnWebRequest webRequest) {
        PlayerTurnRequest request = PlayerTurnRequest.newBuilder()
            .setMatchId(webRequest.matchId())
            .setPlayerName(webRequest.playerName())
            .setCurrentX(webRequest.currentX())
            .setCurrentAngle(webRequest.currentAngle())
            .setDamageDealt(webRequest.damageDealt())
            .addAllTerrain(webRequest.terrain())
            .build();

        blockingStub.playerTurn(request);

        return;
    }

    public MatchHistoryWebResponse loadHistory(String playerName) {
        MatchHistoryRequest request = MatchHistoryRequest.newBuilder()
                .setPlayerName(safe(playerName, "Player"))
                .build();

        MatchHistoryResponse response = blockingStub.loadMatchHistory(request);

        return new MatchHistoryWebResponse(response.getMatchesList());
    }

    private String safe(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value.trim();
    }

    @PreDestroy
    public void shutdown() {
        channel.shutdown();
    }
}
