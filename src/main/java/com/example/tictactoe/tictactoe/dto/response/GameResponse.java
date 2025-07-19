package com.example.tictactoe.tictactoe.dto.response;

import com.example.tictactoe.tictactoe.model.GameState;

public class GameResponse {
    public boolean success;
    public String error;
    public GameState game;
    public GameResponse(boolean success, String error, GameState game) {
        this.success = success;
        this.error = error;
        this.game = game;
    }
} 