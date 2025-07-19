package com.example.tictactoe.tictactoe.service;

import com.example.tictactoe.tictactoe.model.GameState;
import com.example.tictactoe.tictactoe.dto.ErrorCode;

public class GameValidation {
    public static ErrorCode validateJoin(GameState game, String playerId) {
        if (playerId == null || playerId.trim().isEmpty()) {
            return ErrorCode.INVALID_PLAYER_ID;
        }
        if (game == null) {
            return ErrorCode.GAME_NOT_FOUND;
        }
        if (game.getPlayer1().equals(playerId)) {
            return ErrorCode.ALREADY_REGISTERED_P1;
        }
        if (game.getPlayer2() != null && game.getPlayer2().equals(playerId)) {
            return ErrorCode.ALREADY_REGISTERED_P2;
        }
        return ErrorCode.NONE;
    }

    public static ErrorCode validateMove(GameState game, String playerId, int row, int col) {
        if (playerId == null || playerId.trim().isEmpty()) {
            return ErrorCode.INVALID_PLAYER_ID;
        }
        if (game == null) {
            return ErrorCode.GAME_NOT_FOUND;
        }
        if (!playerId.equals(game.getPlayer1()) && !playerId.equals(game.getPlayer2())) {
            return ErrorCode.NOT_A_PLAYER;
        }
        if (game.getStatus() != GameState.Status.IN_PROGRESS) {
            return ErrorCode.GAME_NOT_IN_PROGRESS;
        }
        if (!playerId.equals(game.getCurrentTurn())) {
            return ErrorCode.NOT_YOUR_TURN;
        }
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            return ErrorCode.INVALID_MOVE_POSITION;
        }
        if (game.getBoard()[row][col] != ' ') {
            return ErrorCode.CELL_OCCUPIED;
        }
        return ErrorCode.NONE;
    }
} 