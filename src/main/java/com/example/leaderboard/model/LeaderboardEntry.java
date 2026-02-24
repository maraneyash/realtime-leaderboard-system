package com.example.leaderboard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "leaderboard_entries")
public class LeaderboardEntry {

    @Id
    private String id;

    @Indexed(unique = true)
    private String playerId;

    private int score;

    private Instant updatedAt;

    public LeaderboardEntry() {
    }

    public LeaderboardEntry(String playerId, int score, Instant updatedAt) {
        this.playerId = playerId;
        this.score = score;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
