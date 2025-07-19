package com.example.tictactoe.tictactoe;

import com.example.tictactoe.tictactoe.model.GameState;
import com.example.tictactoe.tictactoe.dto.ResultDTO;
import com.example.tictactoe.tictactoe.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    @Test
    void testCreateGame() {
        GameState game = gameService.createGame("p1");
        assertNotNull(game);
        assertEquals("p1", game.getPlayer1());
        assertNull(game.getPlayer2());
        assertEquals(GameState.Status.WAITING_FOR_PLAYER, game.getStatus());
    }

    @Test
    void testJoinAvailableGame() {
        gameService.createGame("p1");
        ResultDTO.JoinGameResult result = gameService.joinAvailableGame("p2");
        assertNotNull(result.getGame());
        assertNull(result.getError());
        assertEquals("p2", result.getGame().getPlayer2());
        assertEquals(GameState.Status.IN_PROGRESS, result.getGame().getStatus());
    }

    @Test
    void testJoinGameById() {
        GameState game = gameService.createGame("p1");
        ResultDTO.JoinGameResult result = gameService.joinGameById(game.getGameId(), "p2");
        assertNotNull(result.getGame());
        assertNull(result.getError());
        assertEquals("p2", result.getGame().getPlayer2());
    }

    @Test
    void testJoinGameById_Full() {
        GameState game = gameService.createGame("p1");
        gameService.joinGameById(game.getGameId(), "p2");
        ResultDTO.JoinGameResult result = gameService.joinGameById(game.getGameId(), "p3");
        assertNull(result.getGame());
        assertEquals("Game is already full or in progress", result.getError());
    }

    @Test
    void testMakeMove_Win() {
        GameState game = gameService.createGame("p1");
        gameService.joinGameById(game.getGameId(), "p2");
        // X X X
        // O O
        gameService.makeMove(game.getGameId(), "p1", 0, 0);
        gameService.makeMove(game.getGameId(), "p2", 1, 0);
        gameService.makeMove(game.getGameId(), "p1", 0, 1);
        gameService.makeMove(game.getGameId(), "p2", 1, 1);
        ResultDTO.MoveResult winResult = gameService.makeMove(game.getGameId(), "p1", 0, 2);
        assertNotNull(winResult.getGame());
        assertNull(winResult.getError());
        assertEquals(GameState.Status.FINISHED, winResult.getGame().getStatus());
        assertEquals("p1", winResult.getGame().getWinner());
    }

    @Test
    void testMakeMove_Draw() {
        GameState game = gameService.createGame("p1");
        gameService.joinGameById(game.getGameId(), "p2");
        // X O X
        // X O O
        // O X X
        gameService.makeMove(game.getGameId(), "p1", 0, 0); // X
        gameService.makeMove(game.getGameId(), "p2", 0, 1); // O
        gameService.makeMove(game.getGameId(), "p1", 0, 2); // X
        gameService.makeMove(game.getGameId(), "p2", 1, 1); // O
        gameService.makeMove(game.getGameId(), "p1", 1, 0); // X
        gameService.makeMove(game.getGameId(), "p2", 1, 2); // O
        gameService.makeMove(game.getGameId(), "p1", 2, 1); // X
        gameService.makeMove(game.getGameId(), "p2", 2, 0); // O
        ResultDTO.MoveResult drawResult = gameService.makeMove(game.getGameId(), "p1", 2, 2); // X
        assertNotNull(drawResult.getGame());
        assertNull(drawResult.getError());
        assertEquals(GameState.Status.FINISHED, drawResult.getGame().getStatus());
        assertNull(drawResult.getGame().getWinner());
    }

    @Test
    void testMakeMove_InvalidPlayer() {
        GameState game = gameService.createGame("p1");
        gameService.joinGameById(game.getGameId(), "p2");
        ResultDTO.MoveResult result = gameService.makeMove(game.getGameId(), "p3", 0, 0);
        assertNull(result.getGame());
        assertEquals("You are not a player in this game", result.getError());
    }

    @Test
    void testMakeMove_NotYourTurn() {
        GameState game = gameService.createGame("p1");
        gameService.joinGameById(game.getGameId(), "p2");
        gameService.makeMove(game.getGameId(), "p1", 0, 0);
        ResultDTO.MoveResult result = gameService.makeMove(game.getGameId(), "p1", 0, 1);
        assertNull(result.getGame());
        assertEquals("It's not your turn", result.getError());
    }

    @Test
    void testMakeMove_CellOccupied() {
        GameState game = gameService.createGame("p1");
        gameService.joinGameById(game.getGameId(), "p2");
        gameService.makeMove(game.getGameId(), "p1", 0, 0);
        ResultDTO.MoveResult result = gameService.makeMove(game.getGameId(), "p2", 0, 0);
        assertNull(result.getGame());
        assertEquals("Cell already occupied", result.getError());
    }

    @Test
    void testThirdPlayerCannotJoinPlayedGame() {
        GameState game = gameService.createGame("p1");
        gameService.joinGameById(game.getGameId(), "p2");
        // Game is now in progress with two players
        ResultDTO.JoinGameResult result = gameService.joinGameById(game.getGameId(), "p3");
        assertNull(result.getGame());
        assertEquals("Game is already full or in progress", result.getError());
    }

    @Test
    void testSamePlayerCannotJoinTwice() {
        GameState game = gameService.createGame("p1");
        ResultDTO.JoinGameResult result = gameService.joinGameById(game.getGameId(), "p1");
        assertNull(result.getGame());
        assertEquals("You are already registered as player 1 in this game", result.getError());
    }

    @Test
    void testJoinWithInvalidPlayerId() {
        GameState game = gameService.createGame("p1");
        ResultDTO.JoinGameResult result = gameService.joinGameById(game.getGameId(), "");
        assertNull(result.getGame());
        assertEquals("Invalid player ID", result.getError());
    }

    @Test
    void testJoinNonExistentGame() {
        ResultDTO.JoinGameResult result = gameService.joinGameById("not-a-game", "p2");
        assertNull(result.getGame());
        assertEquals("Game not found", result.getError());
    }

    @Test
    void testMoveWithInvalidCoordinates() {
        GameState game = gameService.createGame("p1");
        gameService.joinGameById(game.getGameId(), "p2");
        ResultDTO.MoveResult result = gameService.makeMove(game.getGameId(), "p1", -1, 0);
        assertNull(result.getGame());
        assertEquals("Invalid move position", result.getError());
        result = gameService.makeMove(game.getGameId(), "p1", 0, 3);
        assertNull(result.getGame());
        assertEquals("Invalid move position", result.getError());
    }

    @Test
    void testMoveAfterGameFinished() {
        GameState game = gameService.createGame("p1");
        gameService.joinGameById(game.getGameId(), "p2");
        // Win the game
        gameService.makeMove(game.getGameId(), "p1", 0, 0);
        gameService.makeMove(game.getGameId(), "p2", 1, 0);
        gameService.makeMove(game.getGameId(), "p1", 0, 1);
        gameService.makeMove(game.getGameId(), "p2", 1, 1);
        gameService.makeMove(game.getGameId(), "p1", 0, 2);
        // Try to move after finished
        ResultDTO.MoveResult result = gameService.makeMove(game.getGameId(), "p2", 2, 2);
        assertNull(result.getGame());
        assertEquals("Game is not in progress", result.getError());
    }

    @Test
    void testMoveBeforeBothPlayersJoined() {
        GameState game = gameService.createGame("p1");
        ResultDTO.MoveResult result = gameService.makeMove(game.getGameId(), "p1", 0, 0);
        assertNull(result.getGame());
        assertEquals("Game is not in progress", result.getError());
    }

    @Test
    void testMoveWithEmptyPlayerId() {
        GameState game = gameService.createGame("p1");
        gameService.joinGameById(game.getGameId(), "p2");
        ResultDTO.MoveResult result = gameService.makeMove(game.getGameId(), "", 0, 0);
        assertNull(result.getGame());
        assertEquals("Invalid player ID", result.getError());
    }

    @Test
    void testMoveInNonExistentGame() {
        ResultDTO.MoveResult result = gameService.makeMove("not-a-game", "p1", 0, 0);
        assertNull(result.getGame());
        assertEquals("Game not found", result.getError());
    }

    @Test
    void testPlayerIdCaseSensitivity() {
        GameState game = gameService.createGame("Player1");
        ResultDTO.JoinGameResult result = gameService.joinGameById(game.getGameId(), "player1");
        // Should allow, as IDs are case-sensitive
        assertNotNull(result.getGame());
        assertNull(result.getError());
        assertEquals("player1", result.getGame().getPlayer2());
    }
} 