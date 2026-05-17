const apiBaseUrl = "/web/matches";

let currentMatchId = null;
let currentPlayerName = "Player";

const playerNameInput = document.querySelector("#playerNameInput");
const difficultySelect = document.querySelector("#difficultySelect");
const rankedCheckbox = document.querySelector("#rankedCheckbox");
const botOpponentCheckbox = document.querySelector("#botOpponentCheckbox");

const turnXInput = document.querySelector("#turnXInput");
const turnRotationInput = document.querySelector("#turnRotationInput");
const turnDamageInput = document.querySelector("#turnDamageInput");

const matchIdSpan = document.querySelector("#matchId");
const playerNameSpan = document.querySelector("#playerName");
const opponentNameSpan = document.querySelector("#opponentName");
const winnerNameSpan = document.querySelector("#winnerName");
const log = document.querySelector("#log");

// Necessary for decoding raw bytes sent by SSE
const decoder = new TextDecoder("utf-8");

document.querySelector("#joinButton").addEventListener("click", joinMatch);
document.querySelector("#attackButton").addEventListener("click", sendAttack);
document.querySelector("#historyButton").addEventListener("click", loadHistory);
document.querySelector("#resetButton").addEventListener("click", resetLocalView);

function getPlayerName() {
    const typedName = playerNameInput.value.trim();
    return typedName.length === 0 ? "Player" : typedName;
}

async function joinMatch() {
    currentPlayerName = getPlayerName();

    playerNameSpan.textContent = getPlayerName();

    const request = {
        playerName: currentPlayerName,
        difficulty: difficultySelect.value,
        ranked: rankedCheckbox.checked,
        botOpponent: botOpponentCheckbox.checked
    };

    appendLog("Joining match through web client REST facade...");

    try {
        const response = await fetch(apiBaseUrl, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(request)
        });

        if (!response.ok) throw new Error("Could not join match.");

        appendLog("Connected to Match!");

        await handleMatchUpdates(response);
    } catch (error) {
        appendLog("Error: " + error.message);
    }
}

async function handleMatchUpdates(response) {
    for await(const packet of response.body) {
        const sseRawEvent = decoder.decode(packet, { stream: true })
        const lines = sseRawEvent.split('\n');

        for (let line of lines) {
            if (line.startsWith("{")) {
                const data = JSON.parse(line.trimEnd());

                changeMatchInfo(data)

                switch (data.status) {
                    case "STATUS_WAITING": {
                        appendLog("Waiting for server...");
                        break;
                    }
                    case "STATUS_READY": {
                        handleGamePacket(data);
                        break;
                    }
                    case "STATUS_FINISHED": {
                        return;
                    }
                    default: {
                        throw new Error("Undefined status");
                    }
                }
            }
        }
    }
}

async function sendAttack() {
    const request = {
        matchId: currentMatchId ?? "",
        playerName: currentPlayerName ?? "Player",
        currentX: turnXInput.value ?? 0,
        damageDealt: turnDamageInput.value ?? 0,
        currentAngle: turnRotationInput.value ?? 0,
        terrain: [0, 0, 0]
    };

    appendLog("Attempting to attack...");

    try {
        const response = await fetch(`${apiBaseUrl}/turn`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(request)
        });
    } catch (error) {
        appendLog("Error: " + error.message);
    }
}

function changeMatchInfo(data) {
    matchIdSpan.textContent = data.matchId;
    currentMatchId = data.matchId
    playerNameSpan.textContent = data.player.username;
    currentPlayerName = data.player.username;
    opponentNameSpan.textContent = data.opponent.username;
}

function handleGamePacket(data) {
    appendLog(JSON.stringify(data));
}

async function playMatch() {
    if (!currentMatchId) {
        appendLog("Join a match before playing.");
        return;
    }

    // Necessary for decoding raw bytes sent by SSE
    const decoder = new TextDecoder("utf-8");

    appendLog("Connecting to match...");

    try {
        const response = await fetch(`${apiBaseUrl}/${currentMatchId}/play?playerName=${encodeURIComponent(currentPlayerName)}`, {
            method: "POST"
        });

        if (!response.ok) return;

        appendLog("Connected!");

        for await (const packet of response.body) {
            const sseRawEvent = decoder.decode(packet, { stream: true })
            const lines = sseRawEvent.split('\n');

            for (let line of lines) {
                if (line.startsWith("{")) {
                    appendLog(line.trimEnd());
                }
            }
        }
    } catch (error) {
        appendLog("Error: " + error.message);
    }
}

async function loadHistory() {
    const playerName = encodeURIComponent(getPlayerName());
    appendLog("Loading persisted match history through gRPC...");

    try {
        const response = await fetch(`${apiBaseUrl}/history?playerName=${playerName}`);
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || "Could not load history.");

        appendLog("Match history:");
        data.matches.forEach(match => appendLog("- " + match));
    } catch (error) {
        appendLog("Error: " + error.message);
    }
}

function resetLocalView() {
    currentMatchId = null;
    currentPlayerName = "Player";
    matchIdSpan.textContent = "None";
    playerNameSpan.textContent = "Player";
    opponentNameSpan.textContent = "Opponent";
    winnerNameSpan.textContent = "TBD";
    log.textContent = "";
    appendLog("Local view reset.");
}

function appendLog(message) {
    log.textContent += message + "\n";
}
