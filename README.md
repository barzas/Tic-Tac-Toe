# Tic-Tac-Toe (Spring Boot, WebSocket)

A real-time, two-player Tic-Tac-Toe game built with Java, Spring Boot, and WebSocket. Multiple clients can connect, but only two can play in the same game at a time. The backend manages game state, player registration, moves, and broadcasts updates to all connected clients.

## Features
- Real-time gameplay using WebSocket (STOMP/SockJS)
- Multiple games and clients supported
- Only two players per game
- Join by game ID or auto-join any available game
- Clear error handling and status updates
- Clean separation of DTOs, validation, and service logic

## Tech Stack
- Java 17+ (or compatible)
- Spring Boot
- Spring WebSocket (STOMP, SockJS)
- Maven

## Project Structure
```
src/main/java/com/example/tictactoe/tictactoe/
├── controller/         # WebSocket endpoints (GameController)
├── dto/
│   ├── request/        # DTOs for incoming WebSocket messages
│   ├── response/       # DTOs for outgoing WebSocket responses
│   └── ResultDTO.java  # Result wrappers for service operations
├── model/              # Core game state (GameState)
├── service/            # Game logic, validation utilities
│   ├── GameService.java
│   └── GameValidation.java
└── TictactoeApplication.java
```

## Setup & Run
1. **Clone the repository**
   ```sh
   git clone <repo-url>
   cd Tic-Tac-Toe
   ```
2. **Build the project**
   ```sh
   ./mvnw clean install
   ```
3. **Run the application**
   ```sh
   ./mvnw spring-boot:run
   ```
   The server will start on `http://localhost:8080` by default.

## Architecture
- **Game creation and joining:** via REST API
- **Gameplay (moves, updates):** via WebSocket (STOMP)

## REST API
| Method | Endpoint           | Body Example                                 | Description                |
|--------|--------------------|----------------------------------------------|----------------------------|
| POST   | /api/game          | { "playerId": "player1" }                  | Create a new game          |
| POST   | /api/game/join     | { "playerId": "player2", "gameId": "..." } | Join a specific game       |
| POST   | /api/game/join-any | { "playerId": "player2" }                  | Join any available game    |

## WebSocket API
- **Endpoint:** `/ws` (SockJS/STOMP)
- **Per-game topic:** `/topic/game/{gameId}` (subscribe after REST join/create)
- **App Prefix:** `/app`
- **Moves:** Send to `/app/move` with `{ gameId, playerId, row, col }`

## Client Flow
1. **Connect** (WebSocket): Establish a connection, but do not subscribe yet.
2. **Create or Join Game** (REST):
    - Call `POST /api/game` to create, or `POST /api/game/join`/`join-any` to join.
    - On success, get `gameId` and subscribe to `/topic/game/{gameId}`.
3. **Play**: Send moves via WebSocket, receive real-time updates on the per-game topic.

## Example REST Usage
```sh
# Create a game
curl -X POST http://localhost:8080/api/game -H 'Content-Type: application/json' -d '{"playerId":"player1"}'

# Join a game
curl -X POST http://localhost:8080/api/game/join -H 'Content-Type: application/json' -d '{"playerId":"player2", "gameId":"..."}'

# Join any available game
curl -X POST http://localhost:8080/api/game/join-any -H 'Content-Type: application/json' -d '{"playerId":"player2"}'
```

## Example WebSocket Usage
- Subscribe to `/topic/game/{gameId}` after REST join/create.
- Send moves to `/app/move` as before.

## How to Play (Client Example)
1. Connect to `/ws` using a STOMP WebSocket client.
2. Subscribe to `/topic/game` to receive updates.
3. Send a message to `/app/create` or `/app/joinAnyGame` or `/app/joinByGameId` to join a game.
4. Use `/app/move` to make moves.


## Simple Web UI
A minimal web interface is included for quick testing and demo:
- **Location:** `src/main/resources/static/index.html`
- **How to use:**
  1. Start the Spring Boot server.
  2. Open [http://localhost:8080/](http://localhost:8080/) in your browser.
  3. Enter a player ID, connect, and use the buttons to create/join games and play.
  4. Open in two tabs/windows for two players.
- **Features:**
  - Connect as any player ID
  - Create/join games (by any or by ID)
  - See game state, board, turn, and winner
  - Click to make moves when it's your turn
  - Log of server messages and errors

