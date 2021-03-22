package chess.view;

import chess.domain.board.Board;
import chess.domain.board.Square;
import chess.domain.piece.Color;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.domain.statistics.ChessGameStatistics;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class OutputView {

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void printInitialMessage() {
        System.out.println(
                "> 체스 게임을 시작합니다.\n" +
                        "> 게임 시작 : start\n" +
                        "> 게임 종료 : end\n" +
                        "> 게임 이동 : move source위치 target위치 - 예. move b2 b3"
        );
    }

    public static void printBoard(Board board) {
        for (Rank rank : Rank.asListInReverseOrder()) {
            String line = Arrays.stream(File.values())
                    .map(file -> Position.of(file, rank))
                    .map(board::findByPosition)
                    .map(Square::getNotation)
                    .collect(Collectors.joining());
            System.out.println(line);
        }
    }

    public static void printResult(ChessGameStatistics chessGameStatistics) {
        Map<Color, Double> colorsScore = chessGameStatistics.getColorsScore();
        System.out.printf("게임 결과 : %s%n", chessGameStatistics.getResultText());
        System.out.printf("백 점수 : %.1f%n", colorsScore.get(Color.WHITE));
        System.out.printf("흑 점수 : %.1f%n", colorsScore.get(Color.BLACK));
    }

    public static void printTerminationMessage() {
        System.out.println("종료합니다.");
    }

    public static void printKingDeadMessage() {
        System.out.println("킹이 잡혔습니다.");
    }
}
