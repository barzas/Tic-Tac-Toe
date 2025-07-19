package com.example.tictactoe.tictactoe.dto;

import com.example.tictactoe.tictactoe.model.GameState;

public class ResultDTO {
    public static class JoinGameResult {
        private final GameState game;
        private final ErrorCode errorCode;
        public JoinGameResult(GameState game, ErrorCode errorCode) {
            this.game = game;
            this.errorCode = errorCode;
        }
        public GameState getGame() { return game; }
        public ErrorCode getErrorCode() { return errorCode; }
        public String getError() { return errorCode != null ? errorCode.getMessage() : null; }
    }

    public static class MoveResult {
        private final GameState game;
        private final ErrorCode errorCode;
        public MoveResult(GameState game, ErrorCode errorCode) {
            this.game = game;
            this.errorCode = errorCode;
        }
        public GameState getGame() { return game; }
        public ErrorCode getErrorCode() { return errorCode; }
        public String getError() { return errorCode != null ? errorCode.getMessage() : null; }
    }
} 