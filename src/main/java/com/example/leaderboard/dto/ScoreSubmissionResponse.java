package com.example.leaderboard.dto;

public record ScoreSubmissionResponse(
        String playerId,
        int score,
        int rank,
        boolean leaderboardUpdated
) {
}
