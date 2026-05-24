package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.server.model.PlayerAccount;
import edu.sdccd.cisc191.server.repository.PlayerAccountRepository;
import edu.sdccd.cisc191.server.service.MatchmakingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(MatchmakingService.class)
public class Module1And5And6Test {

    @Autowired
    private MatchmakingService matchmakingService;

    @Autowired
    private PlayerAccountRepository playerAccountRepository;

    @BeforeEach
    public void setUp() {
        playerAccountRepository.deleteAll();

        playerAccountRepository.save(new PlayerAccount("Zelda", 1500));
        playerAccountRepository.save(new PlayerAccount("Zeta", 2500));
        playerAccountRepository.save(new PlayerAccount("Beta", 2500));
        playerAccountRepository.save(new PlayerAccount("Link", 900));
    }

    @Test
    public void MatchmakingService_getTop2PlayersAlpha() {
        String[][] leaderboard = matchmakingService.getLeaderboardArray();

        String[][] top2Leaderboard = matchmakingService.getTopNPlayersSortedAlpha(leaderboard, 2);

        assertNotNull(top2Leaderboard);
        assertNotEquals(leaderboard, top2Leaderboard);

        assertEquals(2, top2Leaderboard.length);
        assertEquals("Beta", top2Leaderboard[0][0]);
        assertEquals("Zeta", top2Leaderboard[1][0]);
    }

    @Test // Module 5
    public void MatchmakingService_getPlayerRecursive() {
        PlayerAccount player = matchmakingService.recursiveGetPlayerByUsername("Link");
        assertEquals("Link", player.getUsername());
    }

    @Test // Module 6 -> Collections
    public void MatchmakingService_LeaderboardArray_sortAndRetrieveTest() {
        // Uses collections internally
        String[][] leaderboard = matchmakingService.getLeaderboardArray();

        // Intended:
        // [ Beta, 2500 ]
        // [ Zeta, 2500 ]
        // [ Zelda, 1500 ]
        // [ Link, 900 ]

        assertNotNull(leaderboard);
        assertEquals(4, leaderboard.length);

        assertEquals("Beta", leaderboard[0][0]);
        assertEquals("2500", leaderboard[0][1]);

        assertEquals("Zeta", leaderboard[1][0]);
        assertEquals("2500", leaderboard[1][1]);

        assertEquals("Zelda", leaderboard[2][0]);
        assertEquals("1500", leaderboard[2][1]);

        assertEquals("Link", leaderboard[3][0]);
        assertEquals("900", leaderboard[3][1]);
    }
}
