package com.example.leaderboard.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ScoreSubmissionRequest(
        @NotBlank(message = "playerId is required")
        String playerId,

        @Min(value = 0, message = "score must be >= 0")
        int score
) {
}
