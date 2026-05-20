package edu.sdccd.cisc191.server.engine;

import edu.sdccd.cisc191.grpc.MatchStateUpdate;

public class OnlineMatchPlayer {
    private final String username;
    private final boolean isBot;

    private int x = 50;
    private int hp = 100;
    private int angle = 0;

    public OnlineMatchPlayer(String username, boolean isBot) {
        this.username = username;
        this.isBot = isBot;
    }

    public String getUsername() {
        return username;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getHp() {
        return hp;
    }

    public void subtractHp(int hp) {
        this.hp-=hp;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public MatchStateUpdate.Player intoUpdatePlayer() {
        return MatchStateUpdate.Player.newBuilder()
            .setUsername(this.username)
            .setHp(this.hp)
            .setAngle(this.angle)
            .setX(this.x)
            .build();
    }
}