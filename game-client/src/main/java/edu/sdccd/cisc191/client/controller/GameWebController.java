package edu.sdccd.cisc191.client.controller;

import edu.sdccd.cisc191.client.dto.*;
import edu.sdccd.cisc191.client.service.GameGrpcClient;
import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/web/matches")
public class GameWebController {

    private final GameGrpcClient gameGrpcClient;

    public GameWebController(GameGrpcClient gameGrpcClient) {
        this.gameGrpcClient = gameGrpcClient;
    }

    @PostMapping
    public SseEmitter joinMatch(@RequestBody JoinMatchWebRequest request) {
        return gameGrpcClient.joinMatch(request);
    }

    @PostMapping("/turn")
    public void playerTurn(@RequestBody PlayerTurnWebRequest request) {
        gameGrpcClient.playerTurn(request);
    }

    @GetMapping("/history")
    public MatchHistoryWebResponse loadHistory(
            @RequestParam(name = "playerName", defaultValue = "Player") String playerName
    ) {
        return gameGrpcClient.loadHistory(playerName);
    }

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<ErrorWebResponse> handleGrpcError(StatusRuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorWebResponse("Could not reach gRPC server: " + exception.getStatus()));
    }
}