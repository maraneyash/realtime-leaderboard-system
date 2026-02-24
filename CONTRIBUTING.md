# Contributing

Thanks for contributing to this project.

## Development Setup

1. Start MongoDB:
   - `cd backend`
   - `docker compose up -d`
2. Build project:
   - `cd backend`
   - `..\\tools\\apache-maven-3.9.9\\bin\\mvn.cmd clean package`
3. Run app:
   - `cd backend`
   - `java -jar .\\target\\realtime-leaderboard-system-1.0.0.jar`
4. Run frontend:
   - `cd frontend`
   - `npm install`
   - `npm run dev`

## Pull Request Guidelines

- Keep PRs focused and small.
- Include clear commit messages.
- Add/update tests when behavior changes.
- Ensure the project builds before opening a PR.

## Code Style

- Follow existing project naming and package conventions.
- Prefer clear, self-explanatory method and variable names.
