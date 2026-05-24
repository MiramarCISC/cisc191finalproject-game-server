package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.server.model.PlayerAccount;
import edu.sdccd.cisc191.server.repository.PlayerAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class Module4Test {
    @Autowired
    private PlayerAccountRepository playerAccountRepository;

    @Test
    public void playerAccountRepository_saveAndPersist() {
        // Create and save with JPA
        PlayerAccount newPlayer = new PlayerAccount("DatabaseTestUser", 1800);
        PlayerAccount savedPlayer = playerAccountRepository.save(newPlayer);

        assertNotNull(savedPlayer.getId(), "JPA should automatically generate an ID upon saving");

        // Fetch with JPA
        Optional<PlayerAccount> fetchedPlayerOpt = playerAccountRepository.findById(savedPlayer.getId());
        assertTrue(fetchedPlayerOpt.isPresent(), "Player should be successfully loaded from the database");


        PlayerAccount fetchedPlayer = fetchedPlayerOpt.get();
        assertEquals("DatabaseTestUser", fetchedPlayer.getUsername());
        assertEquals(1800, fetchedPlayer.getRating());
    }
}
