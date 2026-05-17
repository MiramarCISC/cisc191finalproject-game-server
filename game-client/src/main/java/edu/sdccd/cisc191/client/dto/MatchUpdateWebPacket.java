package edu.sdccd.cisc191.client.dto;

import edu.sdccd.cisc191.grpc.MatchStateUpdate;

import javax.annotation.Nullable;
import java.util.List;

public record MatchUpdateWebPacket(
    String matchId,
    String status,
    boolean isPlayerTurn,
    Player player,
    Player opponent,
    List<Integer> terrain,
    @Nullable Properties properties
) {
    public record Player(
        String username,
        int x,
        int hp,
        int angle
    ) {
        public static Player from(MatchStateUpdate.Player player) {
            if (player == null) return null;

            return new Player(
                player.getUsername(),
                player.getX(),
                player.getHp(),
                player.getAngle()
            );
        }
    }

    public record Properties(
        String difficulty,
        boolean ranked,
        String message
    ) {
        public static Properties from(MatchStateUpdate.Properties properties) {
            if (properties == null) return null;

            return new Properties(
                properties.getDifficulty(),
                properties.getRanked(),
                properties.getMessage()
            );
        }
    }
}