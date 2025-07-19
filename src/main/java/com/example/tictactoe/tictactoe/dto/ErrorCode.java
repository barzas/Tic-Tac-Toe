package com.example.tictactoe.tictactoe.dto;

public enum ErrorCode {
    INVALID_PLAYER_ID("Invalid player ID"),
    GAME_NOT_FOUND("Game not found"),
    ALREADY_REGISTERED_P1("You are already registered as player 1 in this game"),
    ALREADY_REGISTERED_P2("You are already registered as player 2 in this game"),
    GAME_FULL("Game is already full or in progress"),
    NO_AVAILABLE_GAMES("No available games to join"),
    NOT_A_PLAYER("You are not a player in this game"),
    NOT_YOUR_TURN("It's not your turn"),
    INVALID_MOVE_POSITION("Invalid move position"),
    CELL_OCCUPIED("Cell already occupied"),
    GAME_NOT_IN_PROGRESS("Game is not in progress"),
    NONE(null); // For success

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
