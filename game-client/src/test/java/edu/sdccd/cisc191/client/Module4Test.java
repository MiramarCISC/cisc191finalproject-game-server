package edu.sdccd.cisc191.client;

import edu.sdccd.cisc191.client.net.GameHttpService;
import edu.sdccd.cisc191.client.net.exception.InvalidPlayerException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

@RestClientTest(GameHttpService.class)
public class Module4Test {

    @Autowired
    private GameHttpService gameHttpService;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    public void httpRequest_getPlayerMatches_playerNotFound() {
        long playerId = 1;

        this.mockServer.expect(requestToUriTemplate("http://localhost:8080/api/players/{playerId}/matches", playerId))
            .andRespond(withServerError());

        assertThrows(InvalidPlayerException.class, () -> {
           gameHttpService.fetchMatchesForPlayer(playerId);
        });
    }
}
