package edu.sdccd.cisc191.client.dto;

import java.util.List;

public record PlayerTurnWebRequest(
    String matchId,
    String playerName,
    int currentX,
    int damageDealt,
    int currentAngle,
    List<Integer> terrain
) { }
