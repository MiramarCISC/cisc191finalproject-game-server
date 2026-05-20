package edu.sdccd.cisc191.server.service;

import edu.sdccd.cisc191.server.engine.OnlineMatch;
import edu.sdccd.cisc191.server.engine.OnlineMatchPlayer;
import edu.sdccd.cisc191.server.engine.OnlineMatchTurn;
import edu.sdccd.cisc191.server.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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

    public JoinResult joinOrCreateMatch(String playerName, String difficulty, boolean ranked) {
        String matchId = waitingMatches.keySet().stream().findFirst().orElse(null);
        OnlineMatch match;

        if (matchId == null) {
            matchId = UUID.randomUUID().toString();
            match = new OnlineMatch();
            waitingMatches.put(matchId, match);
        } else {
            match = waitingMatches.get(matchId);
        }

        match.addPlayer(playerName, false);
        int count = match.addPlayer("Bot (" + difficulty + ")", true);

        boolean isFull = (count >= 2);
        if (isFull) {
            initializeMatch(matchId);
        }

        return new JoinResult(matchId, match, isFull);
    }

    public boolean validateAndProcessTurn(
        OnlineMatchTurn turn
    ) {
        OnlineMatch match = activeMatches.get(turn.matchId());

        if (match != null) {
            synchronized (match) {
                OnlineMatchPlayer turnPlayer = match.isPlayerTurn()? match.getPlayer(): match.getOpponent();
                OnlineMatchPlayer turnOpponent = match.isPlayerTurn()? match.getOpponent(): match.getPlayer();

                if (turn.playerName().equals(turnPlayer.getUsername())) {
                    turnPlayer.setX(turn.currentX());
                    turnPlayer.setAngle(turn.currentAngle());

                    turnOpponent.subtractHp(turn.damageDealt());

                    match.setTerrain(turn.terrain());

                    match.flipPlayerTurn();

                    broadcaster.broadcastMatchUpdate(turn.matchId(), match, () -> {});

                    if (match.isBotOpponent() && turnOpponent.getUsername().startsWith("Bot")) {
                        fakeBotTurn(turn.matchId(), turnOpponent, turn.terrain());
                    }
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    public void fakeBotTurn(String matchId, OnlineMatchPlayer botPlayer, List<Integer> terrain) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                validateAndProcessTurn(new OnlineMatchTurn(
                    matchId,
                    botPlayer.getUsername(),
                    botPlayer.getX() + (int) Math.floor(Math.random()*30) - 15,
                    (int) Math.floor(Math.random()*10),
                    botPlayer.getAngle(),
                    terrain
                ));
            }
        }, 5000);
    }

    public void initializeMatch(String matchId) {
        OnlineMatch match = waitingMatches.remove(matchId);

        if (match != null) {
            activeMatches.put(matchId, match);
        }
    }
}
