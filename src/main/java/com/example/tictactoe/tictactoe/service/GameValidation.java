package com.example.tictactoe.tictactoe.service;

import com.example.tictactoe.tictactoe.model.GameState;

public class GameValidation {
    public static String validateJoin(GameState game, String playerId) {
        if (playerId == null || playerId.trim().isEmpty()) {
            return "Invalid player ID";
        }
        if (game == null) {
            return "Game not found";
        }
        if (game.getPlayer1().equals(playerId)) {
            return "You are already registered as player 1 in this game";
        }
        if (game.getPlayer2() != null && game.getPlayer2().equals(playerId)) {
            return "You are already registered as player 2 in this game";
        }
        return null;
    }

    public static String validateMove(GameState game, String playerId, int row, int col) {
        if (game == null) {
            return "Game not found";
        }
        if (game.getStatus() != GameState.Status.IN_PROGRESS) {
            return "Game is not in progress";
        }
        if (!playerId.equals(game.getPlayer1()) && !playerId.equals(game.getPlayer2())) {
            return "You are not a player in this game";
        }
        if (!playerId.equals(game.getCurrentTurn())) {
            return "It's not your turn";
        }
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            return "Invalid move position";
        }
        if (game.getBoard()[row][col] != ' ') {
            return "Cell already occupied";
        }
        return null;
    }
} 