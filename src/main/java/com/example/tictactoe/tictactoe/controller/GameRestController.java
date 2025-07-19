package com.example.tictactoe.tictactoe.controller;

import com.example.tictactoe.tictactoe.dto.request.RegisterRequest;
import com.example.tictactoe.tictactoe.dto.request.JoinByIdRequest;
import com.example.tictactoe.tictactoe.dto.response.GameResponse;
import com.example.tictactoe.tictactoe.dto.ErrorCode;
import com.example.tictactoe.tictactoe.dto.ResultDTO;
import com.example.tictactoe.tictactoe.model.GameState;
import com.example.tictactoe.tictactoe.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("/api/game")
public class GameRestController {
    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public GameResponse createGame(@RequestBody RegisterRequest request) {
        if (request.playerId == null || request.playerId.trim().isEmpty()) {
            return new GameResponse(false, ErrorCode.INVALID_PLAYER_ID, null);
        }
        GameState game = gameService.createGame(request.playerId);
        return new GameResponse(true, ErrorCode.NONE, game);
    }

    @PostMapping("/join")
    public GameResponse joinGame(@RequestBody JoinByIdRequest request) {
        ResultDTO.JoinGameResult result = gameService.joinGameById(request.gameId, request.playerId);
        if (result.getGame() != null) {
            messagingTemplate.convertAndSend("/topic/game/" + result.getGame().getGameId(),
                new GameResponse(true, result.getErrorCode(), result.getGame()));
        }
        return new GameResponse(result.getGame() != null, result.getErrorCode(), result.getGame());
    }

    @PostMapping("/join-any")
    public GameResponse joinAnyGame(@RequestBody RegisterRequest request) {
        ResultDTO.JoinGameResult result = gameService.joinAvailableGame(request.playerId);
        if (result.getGame() != null) {
            messagingTemplate.convertAndSend("/topic/game/" + result.getGame().getGameId(),
                new GameResponse(true, result.getErrorCode(), result.getGame()));
        }
        return new GameResponse(result.getGame() != null, result.getErrorCode(), result.getGame());
    }
} 