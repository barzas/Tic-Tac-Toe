package com.example.tictactoe.tictactoe.dto.response;

import com.example.tictactoe.tictactoe.dto.ErrorCode;
import com.example.tictactoe.tictactoe.model.GameState;

public class GameResponse extends BaseResponse {
    private final GameState game;

    public GameResponse(boolean success, ErrorCode errorCode, GameState game) {
        super(success, errorCode);
        this.game = game;
    }

    public GameState getGame() { return game; }
} 