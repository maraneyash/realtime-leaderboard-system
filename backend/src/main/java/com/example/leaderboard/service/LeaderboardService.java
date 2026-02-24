package com.example.leaderboard.service;

import com.example.leaderboard.dto.LeaderboardItemResponse;
import com.example.leaderboard.dto.PlayerRankResponse;
import com.example.leaderboard.dto.ScoreSubmissionRequest;
import com.example.leaderboard.dto.ScoreSubmissionResponse;
import com.example.leaderboard.exception.ResourceNotFoundException;
import com.example.leaderboard.model.LeaderboardEntry;
import com.example.leaderboard.repository.LeaderboardEntryRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class LeaderboardService {

    private static final Logger logger = LoggerFactory.getLogger(LeaderboardService.class);

    private final LeaderboardEntryRepository repository;

    private final ConcurrentMap<String, Integer> scoreByPlayer = new ConcurrentHashMap<>();

    private final ConcurrentSkipListSet<PlayerScore> rankingSet =
            new ConcurrentSkipListSet<>(Comparator
                    .comparingInt(PlayerScore::score).reversed()
                    .thenComparing(PlayerScore::playerId));

    private final Object rankingLock = new Object();

    private volatile boolean mongoAvailable = true;

    public LeaderboardService(LeaderboardEntryRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void warmUpFromMongo() {
        try {
            List<LeaderboardEntry> all = repository.findAll();
            synchronized (rankingLock) {
                for (LeaderboardEntry entry : all) {
                    PlayerScore playerScore = new PlayerScore(entry.getPlayerId(), entry.getScore());
                    scoreByPlayer.put(entry.getPlayerId(), entry.getScore());
                    rankingSet.add(playerScore);
                }
            }
            mongoAvailable = true;
            logger.info("Leaderboard cache warmed with {} players from MongoDB", all.size());
        } catch (Exception exception) {
            mongoAvailable = false;
            logger.warn("MongoDB unavailable during startup; running with in-memory leaderboard only: {}", exception.getMessage());
        }
    }

    public ScoreSubmissionResponse submitScore(ScoreSubmissionRequest request) {
        String playerId = request.playerId().trim();
        int submittedScore = request.score();

        boolean updated;
        int finalScore;
        int rank;

        synchronized (rankingLock) {
            Integer existingScore = scoreByPlayer.get(playerId);
            updated = existingScore == null || submittedScore > existingScore;

            if (updated) {
                if (existingScore != null) {
                    rankingSet.remove(new PlayerScore(playerId, existingScore));
                }
                scoreByPlayer.put(playerId, submittedScore);
                rankingSet.add(new PlayerScore(playerId, submittedScore));
                finalScore = submittedScore;
            } else {
                finalScore = existingScore;
            }

            rank = calculateRankLocked(playerId);
        }

        if (updated && mongoAvailable) {
            persistHighScoreSafely(playerId, finalScore);
        }

        return new ScoreSubmissionResponse(playerId, finalScore, rank, updated);
    }

    public List<LeaderboardItemResponse> getTopPlayers(int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 1000));

        synchronized (rankingLock) {
            List<LeaderboardItemResponse> top = new ArrayList<>();
            int row = 0;
            int rank = 0;
            Integer previousScore = null;

            for (PlayerScore playerScore : rankingSet) {
                row++;
                if (!Objects.equals(previousScore, playerScore.score())) {
                    rank = row;
                }

                top.add(new LeaderboardItemResponse(rank, playerScore.playerId(), playerScore.score()));
                previousScore = playerScore.score();

                if (top.size() >= safeLimit) {
                    break;
                }
            }
            return top;
        }
    }

    public PlayerRankResponse getPlayerRank(String playerId) {
        String normalized = playerId.trim();

        synchronized (rankingLock) {
            Integer score = scoreByPlayer.get(normalized);
            if (score == null) {
                throw new ResourceNotFoundException("Player not found: " + normalized);
            }
            int rank = calculateRankLocked(normalized);
            return new PlayerRankResponse(normalized, score, rank);
        }
    }

    private int calculateRankLocked(String playerId) {
        int row = 0;
        int rank = 0;
        Integer previousScore = null;

        for (PlayerScore playerScore : rankingSet) {
            row++;
            if (!Objects.equals(previousScore, playerScore.score())) {
                rank = row;
            }
            if (playerScore.playerId().equals(playerId)) {
                return rank;
            }
            previousScore = playerScore.score();
        }

        return -1;
    }

    private void persistHighScoreSafely(String playerId, int score) {
        try {
            Optional<LeaderboardEntry> optionalEntry = repository.findByPlayerId(playerId);
            LeaderboardEntry entry = optionalEntry.orElseGet(LeaderboardEntry::new);
            entry.setPlayerId(playerId);
            entry.setScore(score);
            entry.setUpdatedAt(Instant.now());
            repository.save(entry);
        } catch (Exception exception) {
            mongoAvailable = false;
            logger.warn("Failed to persist score for player {}: {}", playerId, exception.getMessage());
        }
    }

    private record PlayerScore(String playerId, int score) {
    }
}
