package com.example.leaderboard.controller;

import com.example.leaderboard.dto.LeaderboardItemResponse;
import com.example.leaderboard.dto.PlayerRankResponse;
import com.example.leaderboard.dto.ScoreSubmissionRequest;
import com.example.leaderboard.dto.ScoreSubmissionResponse;
import com.example.leaderboard.service.LeaderboardService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leaderboard")
@Validated
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @PostMapping("/submit")
    public ScoreSubmissionResponse submitScore(@Valid @RequestBody ScoreSubmissionRequest request) {
        return leaderboardService.submitScore(request);
    }

    @GetMapping("/top")
    public List<LeaderboardItemResponse> getTopPlayers(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return leaderboardService.getTopPlayers(limit);
    }

    @GetMapping("/rank/{playerId}")
    public PlayerRankResponse getRank(@PathVariable String playerId) {
        return leaderboardService.getPlayerRank(playerId);
    }
}
