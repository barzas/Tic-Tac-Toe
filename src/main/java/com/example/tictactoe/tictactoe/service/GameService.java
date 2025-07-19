package com.example.tictactoe.tictactoe.service;

import com.example.tictactoe.tictactoe.model.GameState;
import com.example.tictactoe.tictactoe.dto.ResultDTO;
import com.example.tictactoe.tictactoe.dto.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    // Map of gameId to GameState
    private final Map<String, GameState> games = new ConcurrentHashMap<>();

    // Create a new game and register the first player
    public GameState createGame(String playerId) {
        GameState game = new GameState(playerId);
        games.put(game.getGameId(), game);
        return game;
    }

    /**
     * Try to join any available game that is waiting for a second player.
     * @param playerId The ID of the player attempting to join.
     * @return JoinGameResult with game or error description.
     */
    public ResultDTO.JoinGameResult joinAvailableGame(String playerId) {
        for (GameState game : games.values()) {
            if (game.getStatus() == GameState.Status.WAITING_FOR_PLAYER && game.getPlayer2() == null) {
                ErrorCode errorCode = GameValidation.validateJoin(game, playerId);
                if (errorCode != ErrorCode.NONE) {
                    return new ResultDTO.JoinGameResult(null, errorCode);
                }
                game.setPlayer2(playerId);
                game.setStatus(GameState.Status.IN_PROGRESS);
                return new ResultDTO.JoinGameResult(game, ErrorCode.NONE);
            }
        }
        return new ResultDTO.JoinGameResult(null, ErrorCode.NO_AVAILABLE_GAMES);
    }

    /**
     * Join a specific game by gameId, with error description if full or invalid.
     * @param gameId The ID of the game to join.
     * @param playerId The ID of the player attempting to join.
     * @return JoinGameResult with game or error description.
     */
    public ResultDTO.JoinGameResult joinGameById(String gameId, String playerId) {
        GameState game = games.get(gameId);
        ErrorCode errorCode = GameValidation.validateJoin(game, playerId);
        if (errorCode != ErrorCode.NONE) {
            return new ResultDTO.JoinGameResult(null, errorCode);
        }
        if (game.getStatus() != GameState.Status.WAITING_FOR_PLAYER || game.getPlayer2() != null) {
            return new ResultDTO.JoinGameResult(null, ErrorCode.GAME_FULL);
        }
        game.setPlayer2(playerId);
        game.setStatus(GameState.Status.IN_PROGRESS);
        return new ResultDTO.JoinGameResult(game, ErrorCode.NONE);
    }

    // Get a game by ID
    public Optional<GameState> getGame(String gameId) {
        return Optional.ofNullable(games.get(gameId));
    }

    /**
     * Attempt to make a move in a game.
     * @param gameId The game ID.
     * @param playerId The player making the move.
     * @param row The row (0-2).
     * @param col The column (0-2).
     * @return MoveResult with updated game or error description.
     */
    public ResultDTO.MoveResult makeMove(String gameId, String playerId, int row, int col) {
        GameState game = games.get(gameId);
        ErrorCode errorCode = GameValidation.validateMove(game, playerId, row, col);
        if (errorCode != ErrorCode.NONE) {
            return new ResultDTO.MoveResult(null, errorCode);
        }
        char mark = playerId.equals(game.getPlayer1()) ? 'X' : 'O';
        game.getBoard()[row][col] = mark;
        // Check for win
        if (checkWin(game.getBoard(), mark)) {
            game.setStatus(GameState.Status.FINISHED);
            game.setWinner(playerId);
            System.out.println("Game over! Winner: " + playerId + " (Game ID: " + gameId + ")");
            return new ResultDTO.MoveResult(game, ErrorCode.NONE);
        }
        // Check for draw
        if (isDraw(game.getBoard())) {
            game.setStatus(GameState.Status.FINISHED);
            game.setWinner(null);
            System.out.println("Game over! Draw (Game ID: " + gameId + ")");
            return new ResultDTO.MoveResult(game, ErrorCode.NONE);
        }
        // Switch turn
        if (game.getCurrentTurn().equals(game.getPlayer1())) {
            game.setCurrentTurn(game.getPlayer2());
        } else {
            game.setCurrentTurn(game.getPlayer1());
        }
        return new ResultDTO.MoveResult(game, ErrorCode.NONE);
    }

    private boolean checkWin(char[][] board, char mark) {
        // Rows, columns, diagonals
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == mark && board[i][1] == mark && board[i][2] == mark) return true;
            if (board[0][i] == mark && board[1][i] == mark && board[2][i] == mark) return true;
        }
        if (board[0][0] == mark && board[1][1] == mark && board[2][2] == mark) return true;
        if (board[0][2] == mark && board[1][1] == mark && board[2][0] == mark) return true;
        return false;
    }

    private boolean isDraw(char[][] board) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ') return false;
        return true;
    }

    // List all games (for debugging or admin)
    public Collection<GameState> getAllGames() {
        return games.values();
    }
} 