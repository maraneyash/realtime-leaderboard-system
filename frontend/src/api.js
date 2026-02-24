const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1/leaderboard';

async function handleResponse(response) {
    if (!response.ok) {
        const text = await response.text();
        throw new Error(text || `Request failed: ${response.status}`);
    }
    return response.json();
}

export async function submitScore(playerId, score) {
    const response = await fetch(`${API_BASE_URL}/submit`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ playerId, score })
    });

    return handleResponse(response);
}

export async function fetchTopPlayers(limit = 10) {
    const response = await fetch(`${API_BASE_URL}/top?limit=${limit}`);
    return handleResponse(response);
}

export async function fetchPlayerRank(playerId) {
    const response = await fetch(`${API_BASE_URL}/rank/${encodeURIComponent(playerId)}`);
    return handleResponse(response);
}
