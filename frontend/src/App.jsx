import { useEffect, useState } from 'react';
import { fetchPlayerRank, fetchTopPlayers, submitScore } from './api';
import './App.css';

function App() {
    const [playerId, setPlayerId] = useState('');
    const [score, setScore] = useState('');
    const [rankPlayerId, setRankPlayerId] = useState('');
    const [topLimit, setTopLimit] = useState(10);

    const [topPlayers, setTopPlayers] = useState([]);
    const [rankResult, setRankResult] = useState(null);
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        loadTopPlayers(topLimit);
    }, []);

    async function loadTopPlayers(limit) {
        setLoading(true);
        setError('');

        try {
            const data = await fetchTopPlayers(limit);
            setTopPlayers(data);
        } catch {
            setError('Failed to load leaderboard. Ensure backend is running.');
        } finally {
            setLoading(false);
        }
    }

    async function onSubmitScore(event) {
        event.preventDefault();
        setError('');
        setMessage('');

        const normalizedPlayerId = playerId.trim();
        const parsedScore = Number(score);

        if (!normalizedPlayerId) {
            setError('Player ID is required.');
            return;
        }

        if (!Number.isInteger(parsedScore) || parsedScore < 0) {
            setError('Score must be a non-negative integer.');
            return;
        }

        setLoading(true);
        try {
            const response = await submitScore(normalizedPlayerId, parsedScore);
            setMessage(`Score processed. ${response.playerId} is now rank ${response.rank}.`);
            setPlayerId('');
            setScore('');
            await loadTopPlayers(topLimit);
        } catch {
            setError('Failed to submit score.');
        } finally {
            setLoading(false);
        }
    }

    async function onFindRank(event) {
        event.preventDefault();
        setError('');
        setMessage('');
        setRankResult(null);

        const normalizedPlayerId = rankPlayerId.trim();
        if (!normalizedPlayerId) {
            setError('Player ID is required to find rank.');
            return;
        }

        setLoading(true);
        try {
            const response = await fetchPlayerRank(normalizedPlayerId);
            setRankResult(response);
        } catch {
            setError('Player not found or backend unavailable.');
        } finally {
            setLoading(false);
        }
    }

    async function onRefreshTop(event) {
        event.preventDefault();

        const limit = Number(topLimit);
        const safeLimit = Number.isInteger(limit) && limit > 0 ? limit : 10;
        setTopLimit(safeLimit);
        await loadTopPlayers(safeLimit);
    }

    return (
        <main className="container">
            <h1>Real-Time Leaderboard</h1>

            <section className="panel">
                <h2>Submit Score</h2>
                <form onSubmit={onSubmitScore} className="form-grid">
                    <input
                        value={playerId}
                        onChange={(event) => setPlayerId(event.target.value)}
                        placeholder="Player ID"
                    />
                    <input
                        value={score}
                        onChange={(event) => setScore(event.target.value)}
                        placeholder="Score"
                        type="number"
                        min="0"
                    />
                    <button type="submit" disabled={loading}>Submit</button>
                </form>
            </section>

            <section className="panel">
                <h2>Get Player Rank</h2>
                <form onSubmit={onFindRank} className="form-grid">
                    <input
                        value={rankPlayerId}
                        onChange={(event) => setRankPlayerId(event.target.value)}
                        placeholder="Player ID"
                    />
                    <button type="submit" disabled={loading}>Find Rank</button>
                </form>
                {rankResult && (
                    <p className="result">
                        {rankResult.playerId} has score {rankResult.score} and rank {rankResult.rank}.
                    </p>
                )}
            </section>

            <section className="panel">
                <h2>Top Players</h2>
                <form onSubmit={onRefreshTop} className="form-grid">
                    <input
                        value={topLimit}
                        onChange={(event) => setTopLimit(event.target.value)}
                        type="number"
                        min="1"
                        placeholder="Limit"
                    />
                    <button type="submit" disabled={loading}>Refresh</button>
                </form>

                <table>
                    <thead>
                        <tr>
                            <th>Rank</th>
                            <th>Player</th>
                            <th>Score</th>
                        </tr>
                    </thead>
                    <tbody>
                        {topPlayers.map((entry) => (
                            <tr key={`${entry.playerId}-${entry.rank}`}>
                                <td>{entry.rank}</td>
                                <td>{entry.playerId}</td>
                                <td>{entry.score}</td>
                            </tr>
                        ))}
                        {topPlayers.length === 0 && (
                            <tr>
                                <td colSpan="3">No data available.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </section>

            {message && <p className="success">{message}</p>}
            {error && <p className="error">{error}</p>}
        </main>
    );
}

export default App;
