package edu.sdccd.cisc191.client.net;

import edu.sdccd.cisc191.client.net.dto.PlayerResponse;
import edu.sdccd.cisc191.client.net.dto.QueueEntryResponse;
import edu.sdccd.cisc191.client.net.exception.InvalidPlayerException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GameHttpService {
    private final RestClient restClient;

    public GameHttpService(RestClient restClient) {
        this.restClient = restClient;
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
}
