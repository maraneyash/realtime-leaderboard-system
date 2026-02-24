package com.example.leaderboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.leaderboard.model.LeaderboardEntry;

public interface LeaderboardEntryRepository extends MongoRepository<LeaderboardEntry, String> {
    @SuppressWarnings("null")
    @Override
    List<LeaderboardEntry> findAll();

    @SuppressWarnings("null")
    @Override
    <S extends LeaderboardEntry> S save(S entity);

    Optional<LeaderboardEntry> findByPlayerId(String playerId);
}
