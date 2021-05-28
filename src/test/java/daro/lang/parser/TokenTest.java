package daro.lang.parser;

import daro.lang.ast.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {

    @Test
    void fixedSourceGetKindToken() {
        Token kind = new Token(TokenKind.ELSE, new Position(0));
        assertEquals(TokenKind.ELSE, kind.getKind());
    }

    @Test
    void fixedSourceGetSourceToken() {
        Token kind = new Token(TokenKind.ELSE, new Position(0));
        assertEquals("else", kind.getSource());
    }

    @Test
    void fixedSourceGetPositionToken() {
        Token kind = new Token(TokenKind.ELSE, new Position(42, 46));
        assertEquals(new Position(42, 46), kind.getPosition());
    }

    @Test
    void getKindToken() {
        Token kind = new Token(TokenKind.INTEGER, new Position(0), "1234");
        assertEquals(TokenKind.INTEGER, kind.getKind());
    }

    @Test
    void getSourceToken() {
        Token kind = new Token(TokenKind.INTEGER, new Position(0), "1234");
        assertEquals("1234", kind.getSource());
    }

    @Test
    void getPositionToken() {
        Token kind = new Token(TokenKind.INTEGER, new Position(42, 46), "1234");
        assertEquals(new Position(42, 46), kind.getPosition());
    }

    @Test
    void failOnMissingFixedSourceToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Token(TokenKind.INTEGER, new Position(0));
        });
    }

    @Test
    void failOnMissingSourceToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Token(TokenKind.INTEGER, new Position(0), null);
        });
    }
}
