package daro.lang.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {

    @Test
    void fixedSourceGetKindToken() {
        Token kind = new Token(TokenKind.ELSE, 0);
        assertEquals(TokenKind.ELSE, kind.getKind());
    }

    @Test
    void fixedSourceGetSourceToken() {
        Token kind = new Token(TokenKind.ELSE, 0);
        assertEquals("else", kind.getSource());
    }

    @Test
    void fixedSourceGetPositionToken() {
        Token kind = new Token(TokenKind.ELSE, 42);
        assertEquals(42, kind.getPosition());
    }

    @Test
    void getKindToken() {
        Token kind = new Token(TokenKind.INTEGER, 0, "1234");
        assertEquals(TokenKind.INTEGER, kind.getKind());
    }

    @Test
    void getSourceToken() {
        Token kind = new Token(TokenKind.INTEGER, 0, "1234");
        assertEquals("1234", kind.getSource());
    }

    @Test
    void getPositionToken() {
        Token kind = new Token(TokenKind.INTEGER, 42, "1234");
        assertEquals(42, kind.getPosition());
    }

    @Test
    void failOnMissingFixedSourceToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Token(TokenKind.INTEGER, 0);
        });
    }

    @Test
    void failOnMissingSourceToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Token(TokenKind.INTEGER, 0, null);
        });
    }

    @Test
    void failOnNegativePositionToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Token(TokenKind.INTEGER, -1, null);
        });
    }
}
