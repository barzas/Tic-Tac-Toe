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

## WebSocket API
- **Endpoint:** `/ws` (SockJS/STOMP)
- **Broker:** `/topic/game` (broadcasts)
- **App Prefix:** `/app`

### Message Mappings
| Action         | Mapping             | Request DTO                | Description                       |
| --------------|---------------------|----------------------------|-----------------------------------|
| Create Game   | `/app/create`       | `RegisterRequest`          | Register and create a new game    |
| Join Any Game | `/app/joinAnyGame`  | `RegisterRequest`          | Join any available game           |
| Join by ID    | `/app/joinByGameId` | `JoinByIdRequest`          | Join a specific game by ID        |
| Make Move     | `/app/move`         | `MoveRequest`              | Make a move in a game             |

### DTO Examples
- **RegisterRequest**
  ```json
  { "playerId": "player1" }
  ```
- **JoinByIdRequest**
  ```json
  { "playerId": "player2", "gameId": "<game-id>" }
  ```
- **MoveRequest**
  ```json
  { "gameId": "<game-id>", "playerId": "player1", "row": 0, "col": 2 }
  ```

### Response DTOs
All responses include `success`, `error` (if any), and the current `game` state.

## How to Play (Client Example)
1. Connect to `/ws` using a STOMP WebSocket client.
2. Subscribe to `/topic/game` to receive updates.
3. Send a message to `/app/create` or `/app/joinAnyGame` or `/app/joinByGameId` to join a game.
4. Use `/app/move` to make moves.

## Extending the Project
- Add a frontend (React, Vue, Angular, etc.) for a complete user experience
- Add authentication and persistent user management
- Store game history in a database
- Add support for spectators or chat

