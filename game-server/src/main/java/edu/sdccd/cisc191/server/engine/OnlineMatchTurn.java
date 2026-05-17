package edu.sdccd.cisc191.server.engine;

import java.util.List;

public record OnlineMatchTurn(
    String matchId,
    String playerName,
    int currentX,
    int damageDealt,
    int currentAngle,
    List<Integer> terrain
) {
}
