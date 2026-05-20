package edu.sdccd.cisc191.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sdccd.cisc191.dto.MatchResponse;
import edu.sdccd.cisc191.dto.PlayerResponse;
import edu.sdccd.cisc191.dto.QueueEntryResponse;
import edu.sdccd.cisc191.model.MatchRecord;
import edu.sdccd.cisc191.model.PlayerAccount;
import edu.sdccd.cisc191.model.QueueEntry;
import edu.sdccd.cisc191.service.MatchmakingService;

@RestController
@RequestMapping("/api")
public class MatchmakingController {

    private final MatchmakingService matchmakingService;

    public MatchmakingController(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    @PostMapping("/players")
    public PlayerResponse registerPlayer(
            @RequestParam String username,
            @RequestParam int rating
    ) {
        return toPlayerResponse(matchmakingService.registerPlayer(username, rating));
    }

    @PostMapping("/queue/{playerId}")
    public QueueEntryResponse enqueuePlayer(@PathVariable Long playerId) {
        return toQueueEntryResponse(matchmakingService.enqueuePlayer(playerId));
    }

    @GetMapping("/queue")
    public List<QueueEntryResponse> getQueue() {
        return matchmakingService.getQueue().stream()
                .map(this::toQueueEntryResponse)
                .toList();
    }

    @PostMapping("/matches")
    public MatchResponse createMatch(
            @RequestParam Long playerOneId,
            @RequestParam Long playerTwoId,
            @RequestParam String arenaName
    ) {
        return toMatchResponse(matchmakingService.createMatch(playerOneId, playerTwoId, arenaName));
    }

    @PostMapping("/matches/{matchId}/finish")
    public MatchResponse finishMatch(
            @PathVariable Long matchId,
            @RequestParam Long winnerId
    ) {
        return toMatchResponse(matchmakingService.finishMatch(matchId, winnerId));
    }

    @GetMapping("/players/{playerId}/matches")
    public List<MatchResponse> getMatchesForPlayer(@PathVariable Long playerId) {
        return matchmakingService.findRecentMatchesForPlayer(playerId).stream()
                .map(this::toMatchResponse)
                .toList();
    }

    private PlayerResponse toPlayerResponse(PlayerAccount player) {
        return new PlayerResponse(player.getId(), player.getUsername(), player.getRating());
    }

    private QueueEntryResponse toQueueEntryResponse(QueueEntry queueEntry) {
        return new QueueEntryResponse(
                queueEntry.getId(),
                queueEntry.getPlayer().getId(),
                queueEntry.getPlayer().getUsername(),
                queueEntry.getJoinedAt()
        );
    }

    private MatchResponse toMatchResponse(MatchRecord match) {
        Long winnerId = match.getWinner() == null ? null : match.getWinner().getId();
        return new MatchResponse(
                match.getId(),
                match.getPlayerOne().getId(),
                match.getPlayerOne().getUsername(),
                match.getPlayerTwo().getId(),
                match.getPlayerTwo().getUsername(),
                winnerId,
                match.getArenaName(),
                match.getStatus().name()
        );
    }
}
