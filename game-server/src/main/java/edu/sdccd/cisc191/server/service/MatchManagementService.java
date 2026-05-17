package edu.sdccd.cisc191.server.service;

import edu.sdccd.cisc191.server.engine.OnlineMatch;
import edu.sdccd.cisc191.server.engine.OnlineMatchPlayer;
import edu.sdccd.cisc191.server.engine.OnlineMatchTurn;
import edu.sdccd.cisc191.server.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MatchManagementService {
    private MatchRepository matchRepository;
    private final GameMatchBroadcaster broadcaster;

    private final ConcurrentHashMap<String, OnlineMatch> activeMatches = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, OnlineMatch> waitingMatches = new ConcurrentHashMap<>();

    public MatchManagementService(MatchRepository matchRepository, GameMatchBroadcaster broadcaster) {
        this.matchRepository = matchRepository;
        this.broadcaster = broadcaster;
    }

    public record JoinResult(String matchId, OnlineMatch match, boolean isMatchFull) { }

    public synchronized JoinResult joinOrCreateMatch(String playerName, String difficulty, boolean ranked) {
        String matchId = waitingMatches.keySet().stream().findFirst().orElse(null);
        OnlineMatch match;

        if (matchId == null) {
            matchId = UUID.randomUUID().toString();
            match = new OnlineMatch();
            waitingMatches.put(matchId, match);
        } else {
            match = waitingMatches.get(matchId);
        }

        int count = match.addPlayer(playerName, false);

        if (true) {
            count = match.addPlayer("Bot (" + difficulty + ")", true);
        }

        boolean isFull = (count >= 2);
        if (isFull) {
            initializeMatch(matchId);
        }

        return new JoinResult(matchId, match, isFull);
    }

    public synchronized boolean validateAndProcessTurn(
        OnlineMatchTurn turn
    ) {
        OnlineMatch match = activeMatches.get(turn.matchId());

        if (match != null) {
            OnlineMatchPlayer turnPlayer = match.isPlayerTurn()? match.getPlayer(): match.getOpponent();
            OnlineMatchPlayer turnOpponent = match.isPlayerTurn()? match.getOpponent(): match.getPlayer();

            if (turn.playerName().equals(turnPlayer.getUsername()) && true) {
                match.flipPlayerTurn();

                turnPlayer.setX(turn.currentX());
                turnPlayer.setAngle(turn.currentAngle());

                turnOpponent.subtractHp(turn.damageDealt());

                match.setTerrain(turn.terrain());

                broadcaster.broadcastMatchUpdate(turn.matchId(), match, () -> {});

                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public void initializeMatch(String matchId) {
        OnlineMatch match = waitingMatches.remove(matchId);

        if (match != null) {
            activeMatches.put(matchId, match);
        }
    }
}
