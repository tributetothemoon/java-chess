package chess.controller;

import chess.domain.ChessGameManager;
import chess.domain.position.Position;
import chess.dto.CommonDto;
import chess.dto.ErrorResponse;
import chess.dto.GameStatusDto;
import chess.dto.StatusCode;
import chess.exception.DomainException;
import com.google.gson.Gson;
import org.json.JSONObject;

import static spark.Spark.*;

public class WebController {
    private final Gson gson = new Gson();
    private final ChessGameManager chessGameManager;

    public WebController(ChessGameManager chessGameManager) {
        this.chessGameManager = chessGameManager;
    }

    public void run() {
        receiveNewGameRequest();
        receiveMoveRequest();
    }

    private void receiveNewGameRequest() {
        get("/newgame", (req, res) -> {
            try {
                chessGameManager.start();
                return gson.toJson(new CommonDto<GameStatusDto>(
                        StatusCode.OK,
                        "New game has been created successfully",
                        GameStatusDto.from(chessGameManager.getBoard(), chessGameManager.getCurrentTurnColor())
                ));
            } catch (DomainException e) {
                return gson.toJson(new CommonDto<ErrorResponse>(
                        StatusCode.BAD_REQUEST,
                        e.getMessage(),
                        new ErrorResponse()
                ));
            }
        });
    }

    private void receiveMoveRequest() {
        post("/move", "application/json", (req, res) -> {
            try {
                JSONObject jsonObject = new JSONObject(req.body());
                String from = jsonObject.getString("from");
                String to = jsonObject.getString("to");

                chessGameManager.move(Position.of(from), Position.of(to));
                return gson.toJson(new CommonDto<GameStatusDto>(
                        StatusCode.OK,
                        "A piece has been moved successfully",
                        GameStatusDto.from(chessGameManager.getBoard(), chessGameManager.getCurrentTurnColor())
                ));
            } catch (DomainException e) {
                return gson.toJson(new CommonDto<ErrorResponse>(
                        StatusCode.BAD_REQUEST,
                        e.getMessage(),
                        new ErrorResponse()
                ));
            }
        });
    }
}
