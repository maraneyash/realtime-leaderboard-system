# Real-Time Leaderboard System

High-performance leaderboard backend built with Java + Spring Boot.

This service supports fast score submissions, top-N retrieval, and player rank lookups with an in-memory ranking engine and MongoDB persistence.

## Tech Stack
- Java 21 (compatible with Java 17+)
- Spring Boot 3
- MongoDB
- Java Collections Framework
  - `ConcurrentHashMap`
  - `ConcurrentSkipListSet`
- Maven
- Postman
- Azure-ready deployment

## Why this design?
- **Low-latency ranking**: in-memory data structures handle fast updates/lookups.
- **Durability**: MongoDB stores leaderboard entries.
- **Resilience**: app starts and serves APIs even if MongoDB is temporarily unavailable.

## Features
- Submit and update player scores in real time
- Return top players with tie-aware ranking
- Fetch current rank for any player
- Validate API input and return structured error responses

## API Reference
Base URL: `http://localhost:8080/api/v1/leaderboard`

### 1) Submit Score
`POST /submit`

Request body:
```json
{
  "playerId": "player-101",
  "score": 980
}
```

Response example:
```json
{
  "playerId": "player-101",
  "score": 980,
  "rank": 1,
  "leaderboardUpdated": true
}
```

### 2) Get Top N
`GET /top?limit=10`

### 3) Get Player Rank
`GET /rank/{playerId}`

## Quick Start

### Option A: Run with Docker MongoDB (recommended)
1. Start MongoDB:
   ```bash
   docker compose up -d
   ```
2. Build and run app:
   ```bash
   .\tools\apache-maven-3.9.9\bin\mvn.cmd clean package
   java -jar .\target\realtime-leaderboard-system-1.0.0.jar
   ```

### Option B: Use local MongoDB installation
1. Ensure MongoDB is running at `mongodb://localhost:27017`.
2. Start app with the same commands above.

## Environment Variables
- `MONGODB_URI` (default: `mongodb://localhost:27017/leaderboarddb?connectTimeoutMS=1000&serverSelectionTimeoutMS=1000&socketTimeoutMS=1000`)
- `PORT` (default: `8080`)

## Postman
- Import collection: `postman/Real-Time-Leaderboard.postman_collection.json`

## Project Structure
```text
src/main/java/com/example/leaderboard
├── controller      # REST API layer
├── dto             # Request/response contracts
├── exception       # Global exception handling
├── model           # MongoDB document models
├── repository      # Spring Data repositories
└── service         # Ranking + business logic
```

## Azure Deployment
1. Provision Azure App Service (Java).
2. Provision Azure Cosmos DB for MongoDB API (or MongoDB on Azure VM).
3. Set `MONGODB_URI` in App Service configuration.
4. Deploy artifact:
   - `target/realtime-leaderboard-system-1.0.0.jar`

## License
This project is released under the MIT License.
