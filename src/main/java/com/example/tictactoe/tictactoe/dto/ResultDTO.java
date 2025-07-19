package com.example.tictactoe.tictactoe.dto;

import com.example.tictactoe.tictactoe.model.GameState;

public class ResultDTO {
    public static class JoinGameResult {
        private final GameState game;
        private final String error;
        public JoinGameResult(GameState game, String error) {
            this.game = game;
            this.error = error;
        }
        public GameState getGame() { return game; }
        public String getError() { return error; }
    }

    public static class MoveResult {
        private final GameState game;
        private final String error;
        public MoveResult(GameState game, String error) {
            this.game = game;
            this.error = error;
        }
        public GameState getGame() { return game; }
        public String getError() { return error; }
    }
} 