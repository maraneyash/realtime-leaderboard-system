package com.example.leaderboard.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.leaderboard.model.LeaderboardEntry;

public interface LeaderboardEntryRepository extends MongoRepository<LeaderboardEntry, String> {
    Optional<LeaderboardEntry> findByPlayerId(String playerId);
}
