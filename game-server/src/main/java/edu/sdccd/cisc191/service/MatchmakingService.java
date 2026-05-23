package edu.sdccd.cisc191.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import edu.sdccd.cisc191.exception.PlayerNotFoundException;
import org.springframework.stereotype.Service;

import edu.sdccd.cisc191.model.MatchRecord;
import edu.sdccd.cisc191.model.MatchStatus;
import edu.sdccd.cisc191.model.PlayerAccount;
import edu.sdccd.cisc191.model.QueueEntry;
import edu.sdccd.cisc191.repository.MatchRecordRepository;
import edu.sdccd.cisc191.repository.PlayerAccountRepository;
import edu.sdccd.cisc191.repository.QueueEntryRepository;

@Service
public class MatchmakingService {

    private final PlayerAccountRepository playerAccountRepository;
    private final QueueEntryRepository queueEntryRepository;
    private final MatchRecordRepository matchRecordRepository;

    public MatchmakingService(
            PlayerAccountRepository playerAccountRepository,
            QueueEntryRepository queueEntryRepository,
            MatchRecordRepository matchRecordRepository
    ) {
        this.playerAccountRepository = playerAccountRepository;
        this.queueEntryRepository = queueEntryRepository;
        this.matchRecordRepository = matchRecordRepository;
    }

    public PlayerAccount registerPlayer(String username, int rating) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Cannot register player with empty or null username");
        }

        if (playerAccountRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                String.format("Player with username %s already exists", username)
            );
        }

        PlayerAccount player = new PlayerAccount(username, rating);
        playerAccountRepository.save(player);

        return player;
    }

    public QueueEntry enqueuePlayer(Long playerId) {
        PlayerAccount player = playerAccountRepository.findById(playerId)
            .orElseThrow(PlayerNotFoundException::new);

        QueueEntry entry = new QueueEntry(player, Instant.now());
        return queueEntryRepository.save(entry);
    }

    public MatchRecord createMatch(Long playerOneId, Long playerTwoId, String arenaName) {
        if (arenaName == null || arenaName.isEmpty()) {
            throw new IllegalArgumentException("Cannot create match with undefined arena.");
        }

        PlayerAccount playerOne = playerAccountRepository.findById(playerOneId)
            .orElseThrow(PlayerNotFoundException::new);

        PlayerAccount playerTwo = playerAccountRepository.findById(playerTwoId)
            .orElseThrow(PlayerNotFoundException::new);

        MatchRecord record = new MatchRecord(playerOne, playerTwo, arenaName, MatchStatus.OPEN);
        return matchRecordRepository.save(record);
    }

    public MatchRecord finishMatch(Long matchId, Long winnerId) {
        MatchRecord match = matchRecordRepository.findById(matchId)
            .orElseThrow(IllegalArgumentException::new);

        PlayerAccount playerWinner = playerAccountRepository.findById(winnerId)
            .orElseThrow(PlayerNotFoundException::new);

        if (!match.getPlayerOne().equals(playerWinner) && !match.getPlayerTwo().equals(playerWinner)) {
            throw new PlayerNotFoundException();
        }

        match.setWinner(playerWinner);
        match.setStatus(MatchStatus.COMPLETED);

        // Explicit saving isn't typically necessary, as JPA handles it automatically.
        return matchRecordRepository.save(match);
    }

    public List<QueueEntry> getQueue() {
        return queueEntryRepository.findAllByOrderByJoinedAtAsc();
    }

    public List<MatchRecord> findRecentMatchesForPlayer(Long playerId) {
        // Checking for Player existence.
        if (!playerAccountRepository.existsById(playerId)) {
            throw new PlayerNotFoundException();
        }

        return matchRecordRepository.findByPlayerOneIdOrPlayerTwoIdOrderByIdDesc(playerId, playerId);
    }

    public String[][] getLeaderboardArray() {
        return playerAccountRepository.findAll().stream()
            .sorted(
                Comparator.comparing(PlayerAccount::getRating).reversed()
                    .thenComparing(PlayerAccount::getUsername)
            ).map(account -> {
                return new String[] {
                    account.getUsername(), Integer.toString(account.getRating())
                };
            }).toArray(String[][]::new);
    }

    public String[][] getTopNPlayersSortedAlpha(String[][] leaderboardArray, int n) {
        String[][] workingArray = Arrays.copyOf(leaderboardArray, n);

        return Arrays.stream(workingArray)
            .sorted(Comparator.comparing(e -> e[0]))
            .toArray(String[][]::new);
    }

    public PlayerAccount recursiveGetPlayerByUsername(String username) {
        List<PlayerAccount> players = playerAccountRepository.findAll();

        players.sort(Comparator.comparing(PlayerAccount::getUsername));

        return recursiveGetPlayerByUsernameHelper(players, username, 0, players.size()-1);
    }

    private PlayerAccount recursiveGetPlayerByUsernameHelper(List<PlayerAccount> players, String username, int low, int high) {
        if (low > high) throw new PlayerNotFoundException();

        int mid = (low + high) / 2;
        int comparison = players.get(mid).getUsername().compareTo(username);

        if (comparison == 0) {
            return players.get(mid);
        } else if (comparison > 0) {
            return recursiveGetPlayerByUsernameHelper(players, username, low, --mid);
        } else {
            return recursiveGetPlayerByUsernameHelper(players, username, ++mid, high);
        }
    }
}
