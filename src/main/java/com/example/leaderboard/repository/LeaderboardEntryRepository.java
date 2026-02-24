package com.example.leaderboard.repository;

import com.example.leaderboard.model.LeaderboardEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LeaderboardEntryRepository extends MongoRepository<LeaderboardEntry, String> {
    Optional<LeaderboardEntry> findByPlayerId(String playerId);
}
