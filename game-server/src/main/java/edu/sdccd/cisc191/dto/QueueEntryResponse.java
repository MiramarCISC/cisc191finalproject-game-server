package edu.sdccd.cisc191.dto;

import java.time.Instant;

public record QueueEntryResponse(Long id, Long playerId, String username, Instant joinedAt) {
}
