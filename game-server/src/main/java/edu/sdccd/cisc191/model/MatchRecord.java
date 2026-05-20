package edu.sdccd.cisc191.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class MatchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private PlayerAccount playerOne;

    @ManyToOne(optional = false)
    private PlayerAccount playerTwo;

    @ManyToOne
    private PlayerAccount winner;

    private String arenaName;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    public MatchRecord() {
    }

    public MatchRecord(PlayerAccount playerOne, PlayerAccount playerTwo, String arenaName, MatchStatus status) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.arenaName = arenaName;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public PlayerAccount getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(PlayerAccount playerOne) {
        this.playerOne = playerOne;
    }

    public PlayerAccount getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(PlayerAccount playerTwo) {
        this.playerTwo = playerTwo;
    }

    public PlayerAccount getWinner() {
        return winner;
    }

    public void setWinner(PlayerAccount winner) {
        this.winner = winner;
    }

    public String getArenaName() {
        return arenaName;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }
}
