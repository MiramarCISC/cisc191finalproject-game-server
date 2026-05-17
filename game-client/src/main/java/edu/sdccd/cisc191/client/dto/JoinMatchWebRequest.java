package edu.sdccd.cisc191.client.dto;

public record JoinMatchWebRequest(
        String playerName,
        String difficulty,
        boolean ranked,
        boolean botOpponent
) {
}
