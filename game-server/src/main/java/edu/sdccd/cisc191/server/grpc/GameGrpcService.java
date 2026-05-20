package edu.sdccd.cisc191.server.grpc;

import com.google.protobuf.Empty;
import edu.sdccd.cisc191.grpc.*;
import edu.sdccd.cisc191.server.engine.OnlineMatchTurn;
import edu.sdccd.cisc191.server.entity.MatchEntity;
import edu.sdccd.cisc191.server.repository.MatchRepository;
import edu.sdccd.cisc191.server.service.MatchBroadcasterService;
import edu.sdccd.cisc191.server.service.MatchManagementService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameGrpcService extends GameServiceGrpc.GameServiceImplBase {

    private final MatchRepository matchRepository;
    private final MatchManagementService matchManagementService;
    private final MatchBroadcasterService broadcasterService;

    public GameGrpcService(MatchRepository matchRepository, MatchManagementService matchManagementService, MatchBroadcasterService matchBroadcasterService) {
        this.matchRepository = matchRepository;
        this.matchManagementService = matchManagementService;
        this.broadcasterService = matchBroadcasterService;
    }

//    @Override
//    public void joinMatch(
//        JoinMatchRequest request,
//        StreamObserver<MatchStateUpdate> responseObserver
//    ) {
//        String playerName = normalizePlayerName(request.getPlayerName());
//        String difficulty = normalizeDifficulty(request.getDifficulty());
//        boolean ranked = request.getRanked();
//
//        String matchId = UUID.randomUUID().toString();
//
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//
//        MatchStateUpdate.Player testPlayer = MatchStateUpdate.Player.newBuilder()
//            .setUsername("test").setHp(4).setAngle(30).setX(40)
//            .build();
//
//        MatchStateUpdate.Properties props = MatchStateUpdate.Properties.newBuilder()
//            .setDifficulty(difficulty)
//            .setRanked(ranked)
//            .setMessage("Test").build();
//
//        System.out.println("Test");
//
//        executor.scheduleAtFixedRate(() -> {
//            System.out.println("Hi");
//
//            responseObserver.onNext(MatchStateUpdate.newBuilder()
//                .setMatchId(matchId)
//                .setStatus(MatchStateUpdate.Status.STATUS_WAITING)
//                .setTurnNumber(1)
//                .setPlayer(testPlayer)
//                .setOpponent(testPlayer)
//                .addTerrain(0)
//                .setProperties(props)
//                .build()
//            );
//        }, 0, 10, TimeUnit.MILLISECONDS);
//    }

    @Override
    public void joinMatch(
        JoinMatchRequest request,
        StreamObserver<MatchStateUpdate> responseObserver
    ) {
        String playerName = normalizePlayerName(request.getPlayerName());
        String difficulty = normalizeDifficulty(request.getDifficulty());
        boolean ranked = request.getRanked();

        MatchManagementService.JoinResult result = matchManagementService.joinOrCreateMatch(playerName, difficulty, ranked);

        broadcasterService.registerClient(result.matchId(), responseObserver);

        MatchStateUpdate.Status matchStatus = result.isMatchFull() ?
            MatchStateUpdate.Status.STATUS_READY : MatchStateUpdate.Status.STATUS_WAITING;

        MatchStateUpdate.Properties props = MatchStateUpdate.Properties.newBuilder()
            .setDifficulty(difficulty)
            .setRanked(ranked)
            .setMessage(result.isMatchFull() ? "Match Starting!" : "Waiting for Opponent...")
            .build();

        MatchStateUpdate initialUpdate = MatchStateUpdate.newBuilder()
            .setMatchId(result.matchId())
            .setStatus(matchStatus)
            .setIsPlayerTurn(true)
            .setProperties(props)
            .setPlayer(result.match().getPlayer().intoUpdatePlayer())
            .setOpponent(result.match().getOpponent().intoUpdatePlayer())
            .build();

        broadcasterService.broadcastUpdate(result.matchId(), initialUpdate, () -> {
            broadcasterService.unregisterClient(result.matchId(), responseObserver);
        });
    }

    @Override
    public void playerTurn(
        PlayerTurnRequest request,
        StreamObserver<Empty> responseObserver
    ) {
        boolean isTurnSuccessful = matchManagementService.validateAndProcessTurn(new OnlineMatchTurn(
            request.getMatchId(),
            request.getPlayerName(),
            request.getCurrentX(),
            request.getDamageDealt(),
            request.getCurrentAngle(),
            request.getTerrainList()
        ));

        if (isTurnSuccessful) {
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            Status status = Status.FAILED_PRECONDITION;

            responseObserver.onError(status.asRuntimeException());
        }
    }

    @Override
    public void loadMatchHistory(
            MatchHistoryRequest request,
            StreamObserver<MatchHistoryResponse> responseObserver
    ) {
        String playerName = normalizePlayerName(request.getPlayerName());

        List<MatchEntity> savedMatches = null;
               // matchRepository.findTop10ByPlayerNameOrderByMatchIdDesc(playerName);

        MatchHistoryResponse.Builder response = MatchHistoryResponse.newBuilder();

        if (savedMatches.isEmpty()) {
            response.addMatches(playerName + " has no saved matches yet.");
        } else {
            for (MatchEntity match : savedMatches) {
                String result;

                if (!match.isComplete()) {
                    result = "Pending";
                } else if (match.getWinnerName().equals(match.getPlayerName())) {
                    result = "Win";
                } else {
                    result = "Loss";
                }

                response.addMatches(match.getPlayerName()
                        + " vs " + match.getOpponentName()
                        + " [" + match.getMatchType() + ", " + match.getDifficulty() + "]"
                        + ": " + result);
            }
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    private String normalizePlayerName(String value) {
        if (value == null || value.isBlank()) {
            return "Player";
        }

        return value.trim();
    }

    private String normalizeDifficulty(String value) {
        if (value == null || value.isBlank()) {
            return "Normal";
        }

        return switch (value.trim().toLowerCase()) {
            case "easy" -> "Easy";
            case "hard" -> "Hard";
            default -> "Normal";
        };
    }
}
