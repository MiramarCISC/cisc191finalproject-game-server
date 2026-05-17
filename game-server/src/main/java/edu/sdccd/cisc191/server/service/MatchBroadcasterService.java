package edu.sdccd.cisc191.server.service;

import edu.sdccd.cisc191.grpc.MatchStateUpdate;
import edu.sdccd.cisc191.server.engine.OnlineMatch;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MatchBroadcasterService implements GameMatchBroadcaster {
    private final ConcurrentHashMap<String, Set<StreamObserver<MatchStateUpdate>>> matchStreams = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void registerClient(String matchId, StreamObserver<MatchStateUpdate> responseObserver) {
        matchStreams.computeIfAbsent(matchId, id -> ConcurrentHashMap.newKeySet(2)).add(responseObserver);
    }

    public void unregisterClient(String matchId, StreamObserver<MatchStateUpdate> responseObserver) {
        matchStreams.computeIfPresent(matchId, (id, set) -> {
            set.remove(responseObserver);

            return set.isEmpty() ? null : set;
        });
    }

    public void broadcastUpdate(String matchId, MatchStateUpdate update, Runnable cleanupCallback) {
        Set<StreamObserver<MatchStateUpdate>> streamObservers = matchStreams.get(matchId);

        if (streamObservers == null) {
            throw new IllegalArgumentException("Match ID " + matchId + " does not exist");
        }

        for (StreamObserver<MatchStateUpdate> streamObserver : streamObservers) {
            executor.submit(() -> {
                try {
                    synchronized (streamObserver) {
                        streamObserver.onNext(update);
                    }
                } catch (Exception e) {
                    cleanupCallback.run();
                }
            });
        }
    }

    public void endBroadcast(String matchId) {
        Set<StreamObserver<MatchStateUpdate>> streamObservers = matchStreams.get(matchId);

        if (streamObservers != null) {
            for (StreamObserver<MatchStateUpdate> streamObserver : streamObservers) {
                executor.submit(() -> {
                    try {
                        synchronized (streamObserver) {
                            streamObserver.onCompleted();
                        }
                    } catch (Exception ignored) {}
                });
            }
        }
    }

    @Override
    public void broadcastMatchUpdate(String matchId, OnlineMatch match, Runnable cleanupCallback) {
        MatchStateUpdate update = MatchStateUpdate.newBuilder()
            .setMatchId(matchId)
            .setStatus(MatchStateUpdate.Status.STATUS_READY)
            .setIsPlayerTurn(match.isPlayerTurn())
            .setPlayer(match.getPlayer().intoUpdatePlayer())
            .setOpponent(match.getOpponent().intoUpdatePlayer())
            //.clearTerrain()
            //.addAllTerrain(match.getTerrain())
            //.setProperties((MatchStateUpdate.Properties) null)
            .clearProperties()
            .build();

        this.broadcastUpdate(matchId, update, cleanupCallback);
    }
}
