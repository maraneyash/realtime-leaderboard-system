package com.example.leaderboard.dto;

public record LeaderboardItemResponse(
        int rank,
        String playerId,
        int score
) {
}
