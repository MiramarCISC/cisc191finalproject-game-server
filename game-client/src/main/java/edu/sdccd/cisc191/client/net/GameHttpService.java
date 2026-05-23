package edu.sdccd.cisc191.client.net;

import edu.sdccd.cisc191.client.net.dto.MatchResponse;
import edu.sdccd.cisc191.client.net.dto.PlayerResponse;
import edu.sdccd.cisc191.client.net.dto.QueueEntryResponse;
import edu.sdccd.cisc191.client.net.exception.InvalidMatchException;
import edu.sdccd.cisc191.client.net.exception.InvalidPlayerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class GameHttpService {
    private final RestClient restClient;

    public GameHttpService(
        RestClient.Builder builder,
        @Value("${api.server.base-url}") String baseUrl,
        @Value("${api.server.port}") String port,
        @Value("${api.server.endpoint}") String endpoint
    ) {
        this.restClient = builder
            .baseUrl(UriComponentsBuilder
                .fromUriString(baseUrl).port(port).path(endpoint)
                .build().toUri()
            ).defaultHeader("Accept", "application/json")
            .build();
    }

    public PlayerResponse registerPlayer(String username, int rating) {
        return restClient.post()
            .uri(builder -> builder
                .path("/players")
                .queryParam("username", username)
                .queryParam("rating", rating)
                .build()
            ).retrieve()
            .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                throw new InvalidPlayerException("Server could not create player.");
            }).body(PlayerResponse.class);
    }

    public QueueEntryResponse enqueuePlayer(Long playerId) {
        return restClient.post()
            .uri("/queue/{playerId}", playerId)
            .retrieve()
            .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                throw new InvalidPlayerException("Player could not be enqueued.");
            }).body(QueueEntryResponse.class);
    }

    public List<QueueEntryResponse> fetchQueue() {
        return restClient.get()
            .uri("/queue")
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});
    }

    public MatchResponse createMatch(Long player1Id, Long player2Id, String arenaName) {
        return restClient.post()
            .uri(builder -> builder
                .path("/matches")
                .queryParam("playerOneId", player1Id)
                .queryParam("playerTwoId", player2Id)
                .queryParam("arenaName", arenaName)
                .build()
            ).retrieve()
            .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                throw new InvalidMatchException("Server could not create match.");
            }).body(MatchResponse.class);
    }

    public MatchResponse finishMatch(Long matchId, Long winnerId) {
        return restClient.post()
            .uri(builder -> builder
                .path("/matches/{matchId}/finish")
                .queryParam("winnerId", winnerId)
                .build(matchId)
            ).retrieve()
            .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                throw new InvalidMatchException("Server could not finish match.");
            }).body(MatchResponse.class);
    }

    public List<MatchResponse> fetchMatchesForPlayer(Long playerId) {
        return restClient.get()
            .uri("/players/{playerId}/matches", playerId)
            .retrieve()
            .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                throw new InvalidPlayerException("Server could not fetch matches.");
            }).body(new ParameterizedTypeReference<>() {});
    }
}
