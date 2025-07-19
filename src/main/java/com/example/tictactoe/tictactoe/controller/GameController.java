package com.example.tictactoe.tictactoe.controller;

import com.example.tictactoe.tictactoe.dto.request.RegisterRequest;
import com.example.tictactoe.tictactoe.dto.request.JoinByIdRequest;
import com.example.tictactoe.tictactoe.dto.request.MoveRequest;
import com.example.tictactoe.tictactoe.dto.response.GameResponse;
import com.example.tictactoe.tictactoe.dto.response.MoveResponse;
import com.example.tictactoe.tictactoe.dto.ResultDTO;
import com.example.tictactoe.tictactoe.model.GameState;
import com.example.tictactoe.tictactoe.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class GameController {
    @Autowired
    private GameService gameService;

    @MessageMapping("/create")
    @SendTo("/topic/game")
    public GameResponse createGame(@Payload RegisterRequest request) {
        if (request.playerId == null || request.playerId.trim().isEmpty()) {
            return new GameResponse(false, "Invalid player ID", null);
        }
        GameState game = gameService.createGame(request.playerId);
        return new GameResponse(true, null, game);
    }

    @MessageMapping("/joinAnyGame")
    @SendTo("/topic/game")
    public GameResponse joinAnyGame(@Payload RegisterRequest request) {
        ResultDTO.JoinGameResult result = gameService.joinAvailableGame(request.playerId);
        return new GameResponse(result.getGame() != null, result.getError(), result.getGame());
    }

    @MessageMapping("/joinByGameId")
    @SendTo("/topic/game")
    public GameResponse joinByGameId(@Payload JoinByIdRequest request) {
        ResultDTO.JoinGameResult result = gameService.joinGameById(request.gameId, request.playerId);
        return new GameResponse(result.getGame() != null, result.getError(), result.getGame());
    }

    @MessageMapping("/move")
    @SendTo("/topic/game")
    public MoveResponse makeMove(@Payload MoveRequest request) {
        ResultDTO.MoveResult result = gameService.makeMove(request.gameId, request.playerId, request.row, request.col);
        return new MoveResponse(result.getGame() != null, result.getError(), result.getGame());
    }
} 