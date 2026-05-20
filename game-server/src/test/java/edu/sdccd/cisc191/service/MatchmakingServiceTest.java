package edu.sdccd.cisc191.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import edu.sdccd.cisc191.model.MatchRecord;
import edu.sdccd.cisc191.model.MatchStatus;
import edu.sdccd.cisc191.model.PlayerAccount;
import edu.sdccd.cisc191.model.QueueEntry;

@SpringBootTest
@Transactional
class MatchmakingServiceTest {

    @Autowired
    private MatchmakingService matchmakingService;

    @Test
    void registerPlayerPersistsAPlayer() {
        PlayerAccount player = matchmakingService.registerPlayer("alpha", 1200);

        assertNotNull(player.getId());
        assertEquals("alpha", player.getUsername());
        assertEquals(1200, player.getRating());
    }

    @Test
    void enqueuePlayerCreatesQueueEntryLinkedToPlayer() {
        PlayerAccount player = matchmakingService.registerPlayer("bravo", 1250);

        QueueEntry entry = matchmakingService.enqueuePlayer(player.getId());

        assertNotNull(entry.getId());
        assertEquals(player.getId(), entry.getPlayer().getId());
        assertNotNull(entry.getJoinedAt());
    }

    @Test
    void createMatchStoresBothPlayersAndArena() {
        PlayerAccount p1 = matchmakingService.registerPlayer("charlie", 1300);
        PlayerAccount p2 = matchmakingService.registerPlayer("delta", 1310);

        MatchRecord match = matchmakingService.createMatch(p1.getId(), p2.getId(), "Volcano");

        assertNotNull(match.getId());
        assertEquals("Volcano", match.getArenaName());
        assertEquals(MatchStatus.OPEN, match.getStatus());
        assertEquals(p1.getId(), match.getPlayerOne().getId());
        assertEquals(p2.getId(), match.getPlayerTwo().getId());
    }

    @Test
    void finishMatchSetsWinnerAndCompletedStatus() {
        PlayerAccount p1 = matchmakingService.registerPlayer("echo", 1350);
        PlayerAccount p2 = matchmakingService.registerPlayer("foxtrot", 1360);
        MatchRecord match = matchmakingService.createMatch(p1.getId(), p2.getId(), "Harbor");

        MatchRecord completed = matchmakingService.finishMatch(match.getId(), p2.getId());

        assertEquals(MatchStatus.COMPLETED, completed.getStatus());
        assertNotNull(completed.getWinner());
        assertEquals(p2.getId(), completed.getWinner().getId());
    }

    @Test
    void finishMatchRejectsWinnerWhoIsNotInTheMatch() {
        PlayerAccount p1 = matchmakingService.registerPlayer("golf", 1400);
        PlayerAccount p2 = matchmakingService.registerPlayer("hotel", 1410);
        PlayerAccount outsider = matchmakingService.registerPlayer("intruder", 1000);
        MatchRecord match = matchmakingService.createMatch(p1.getId(), p2.getId(), "Factory");

        assertThrows(IllegalArgumentException.class,
                () -> matchmakingService.finishMatch(match.getId(), outsider.getId()));
    }

    @Test
    void getQueueReturnsEntriesOrderedByJoinTime() {
        PlayerAccount p1 = matchmakingService.registerPlayer("india", 1200);
        PlayerAccount p2 = matchmakingService.registerPlayer("juliet", 1250);

        matchmakingService.enqueuePlayer(p1.getId());
        matchmakingService.enqueuePlayer(p2.getId());

        List<QueueEntry> queue = matchmakingService.getQueue();

        assertEquals(2, queue.size());
        assertEquals("india", queue.get(0).getPlayer().getUsername());
        assertEquals("juliet", queue.get(1).getPlayer().getUsername());
    }

    @Test
    void findRecentMatchesForPlayerReturnsMatchesWherePlayerParticipated() {
        PlayerAccount p1 = matchmakingService.registerPlayer("kilo", 1500);
        PlayerAccount p2 = matchmakingService.registerPlayer("lima", 1510);
        PlayerAccount p3 = matchmakingService.registerPlayer("mike", 1520);

        matchmakingService.createMatch(p1.getId(), p2.getId(), "Canyon");
        matchmakingService.createMatch(p3.getId(), p1.getId(), "Temple");

        List<MatchRecord> matches = matchmakingService.findRecentMatchesForPlayer(p1.getId());

        assertEquals(2, matches.size());
        assertTrue(matches.stream().allMatch(match ->
                match.getPlayerOne().getId().equals(p1.getId())
                        || match.getPlayerTwo().getId().equals(p1.getId())));
    }
}
