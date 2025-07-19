package com.example.tictactoe.tictactoe.model;

import java.util.Arrays;
import java.util.UUID;

public class GameState {
    public enum Status {
        WAITING_FOR_PLAYER,
        IN_PROGRESS,
        FINISHED
    }

    private final String gameId;
    private String player1;
    private String player2;
    private char[][] board;
    private String currentTurn; // player1 or player2
    private Status status;
    private String winner; // null if no winner yet

    public GameState(String player1) {
        this.gameId = UUID.randomUUID().toString();
        this.player1 = player1;
        this.player2 = null;
        this.board = new char[3][3];
        for (char[] row : board) Arrays.fill(row, ' ');
        this.currentTurn = player1;
        this.status = Status.WAITING_FOR_PLAYER;
        this.winner = null;
    }

    // Getters and setters
    public String getGameId() { return gameId; }
    public String getPlayer1() { return player1; }
    public String getPlayer2() { return player2; }
    public void setPlayer2(String player2) { this.player2 = player2; }
    public char[][] getBoard() { return board; }
    public String getCurrentTurn() { return currentTurn; }
    public void setCurrentTurn(String currentTurn) { this.currentTurn = currentTurn; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
} 