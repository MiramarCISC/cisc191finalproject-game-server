package edu.sdccd.cisc191.server.engine;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class OnlineMatch {
    private OnlineMatchPlayer player;
    private OnlineMatchPlayer opponent;
    private boolean isPlayerTurn = true;
    private boolean isBotOpponent = true;

    private final AtomicReference<List<Integer>> terrain = new AtomicReference<>();

    public OnlineMatch() {}

    public synchronized int addPlayer(String name, boolean isBot) {
        if (player == null) {
            player = new OnlineMatchPlayer(name, isBot);
            return 1;
        } else if (opponent == null) {
            opponent = new OnlineMatchPlayer(name, isBot);
            return 2;
        }

        return 2;
    }

    public synchronized int getPlayerCount() {
        int count = 0;
        if (player != null) count++;
        if (opponent != null) count++;
        return count;
    }

    public synchronized List<Integer> getTerrain() {
        return terrain.get();
    }

    public synchronized void setTerrain(List<Integer> terrain) {
        this.terrain.set(terrain);
    }

    public synchronized boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public synchronized void flipPlayerTurn() {
        isPlayerTurn = !isPlayerTurn;
    }

    public synchronized OnlineMatchPlayer getPlayer() {
        return this.player;
    }

    public synchronized OnlineMatchPlayer getOpponent() {
        return this.opponent;
    }

    public synchronized boolean isBotOpponent() {
        return this.isBotOpponent;
    }
}