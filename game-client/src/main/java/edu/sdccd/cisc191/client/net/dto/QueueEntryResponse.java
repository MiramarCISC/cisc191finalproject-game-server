package edu.sdccd.cisc191.client.net.dto;

import java.time.Instant;

public record QueueEntryResponse(Long id, Long playerId, String username, Instant joinedAt) {
}
