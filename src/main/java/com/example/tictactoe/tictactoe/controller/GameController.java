package com.example.tictactoe.tictactoe.controller;


import com.example.tictactoe.tictactoe.dto.request.MoveRequest;
import com.example.tictactoe.tictactoe.dto.response.MoveResponse;
import com.example.tictactoe.tictactoe.dto.ResultDTO;
import com.example.tictactoe.tictactoe.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {
    @Autowired
    private GameService gameService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/move")
    public void makeMove(@Payload MoveRequest request) {
        ResultDTO.MoveResult result = gameService.makeMove(request.gameId, request.playerId, request.row, request.col);
        if (request.gameId != null) {
            messagingTemplate.convertAndSend("/topic/game/" + request.gameId,
                new MoveResponse(result.getGame() != null, result.getErrorCode(), result.getGame()));
        } else {
            messagingTemplate.convertAndSendToUser(request.playerId, "/queue/game-error",
                new MoveResponse(false, result.getErrorCode(), null));
        }
    }
}