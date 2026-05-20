package edu.sdccd.cisc191.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sdccd.cisc191.model.PlayerAccount;

public interface PlayerAccountRepository extends JpaRepository<PlayerAccount, Long> {
    Optional<PlayerAccount> findByUsername(String username);
    boolean existsByUsername(String username);
}
