package chess.domain.board;

import chess.domain.position.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static chess.domain.piece.Fixture.mockBoard;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SquareTest {
    @DisplayName("move시에 말이 없으면 예외")
    @Test
    void throwExceptionWhenEmptySquare() {
        Square c3 = mockBoard.findByPosition(Position.of("c3"));

        assertThatThrownBy(() -> c3.move(mockBoard.createMoveOrder(mockBoard, Position.of("c3"), Position.of("c4"))))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 위치엔 말이 없습니다.");
    }
}