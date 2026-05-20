package edu.sdccd.cisc191.dto;

public record MatchResponse(
        Long id,
        Long playerOneId,
        String playerOneUsername,
        Long playerTwoId,
        String playerTwoUsername,
        Long winnerId,
        String arenaName,
        String status
) {
}
