package edu.sdccd.cisc191.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sdccd.cisc191.model.MatchRecord;

public interface MatchRecordRepository extends JpaRepository<MatchRecord, Long> {
    List<MatchRecord> findByPlayerOneIdOrPlayerTwoIdOrderByIdDesc(Long playerOneId, Long playerTwoId);
}
