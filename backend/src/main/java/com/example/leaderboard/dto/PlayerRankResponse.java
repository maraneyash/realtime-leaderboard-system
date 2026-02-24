package com.example.leaderboard.dto;

public record PlayerRankResponse(
        String playerId,
        int score,
        int rank
) {
}
