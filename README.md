# Real-Time Leaderboard System

Spring Boot project implementing a high-speed real-time leaderboard.

## Tech Stack
- Java 17
- Spring Boot
- MongoDB
- Java Collections Framework (`ConcurrentHashMap`, `ConcurrentSkipListSet`)
- Maven
- Postman (API testing)
- Azure (deployment-ready)

## Features
- Fast score submission API
- Top N leaderboard API
- Player rank lookup API
- In-memory ranking cache for low-latency updates/lookups
- MongoDB persistence for durable storage

## API Endpoints
Base URL: `http://localhost:8080/api/v1/leaderboard`

1. Submit score
   - `POST /submit`
   - Body:
     ```json
     {
       "playerId": "player-101",
       "score": 980
     }
     ```

2. Get top players
   - `GET /top?limit=10`

3. Get a player's rank
   - `GET /rank/{playerId}`

## Run Locally
### Option A: Local MongoDB
1. Start MongoDB at `mongodb://localhost:27017`.
2. Build and run:
   ```bash
   mvn clean spring-boot:run
   ```

### Option B: Docker MongoDB
1. Start MongoDB using Docker:
   ```bash
   docker compose up -d
   ```
2. Run app:
   ```bash
   mvn clean spring-boot:run
   ```

## Environment Variables
- `MONGODB_URI` (default: `mongodb://localhost:27017/leaderboarddb`)
- `PORT` (default: `8080`)

## Postman Quick Test
- Import endpoints manually or use these sample calls:
  - `POST http://localhost:8080/api/v1/leaderboard/submit`
  - `GET http://localhost:8080/api/v1/leaderboard/top?limit=5`
  - `GET http://localhost:8080/api/v1/leaderboard/rank/player-101`

## Azure Deployment Notes
1. Provision **Azure App Service** (Java 17).
2. Provision **Azure Cosmos DB for MongoDB API** (or Azure-hosted MongoDB).
3. Set App Service environment variable `MONGODB_URI` to your Azure Mongo connection string.
4. Deploy jar:
   ```bash
   mvn clean package
   ```
   Deploy `target/realtime-leaderboard-system-1.0.0.jar`.
