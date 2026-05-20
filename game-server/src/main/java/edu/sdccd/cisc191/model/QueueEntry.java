package edu.sdccd.cisc191.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class QueueEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private PlayerAccount player;

    private Instant joinedAt;

    public QueueEntry() {
    }

    public QueueEntry(PlayerAccount player, Instant joinedAt) {
        this.player = player;
        this.joinedAt = joinedAt;
    }

    public Long getId() {
        return id;
    }

    public PlayerAccount getPlayer() {
        return player;
    }

    public void setPlayer(PlayerAccount player) {
        this.player = player;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }
}
