package edu.sdccd.cisc191.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sdccd.cisc191.model.QueueEntry;

public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {
    List<QueueEntry> findAllByOrderByJoinedAtAsc();
}
