package edu.sdccd.cisc191.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sdccd.cisc191.server.model.PlayerAccount;

public interface PlayerAccountRepository extends JpaRepository<PlayerAccount, Long> {
    boolean existsByUsername(String username);
}
