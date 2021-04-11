package daro.lang.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {

    @Test
    void fixedSourceGetKindToken() {
        Token kind = new Token(TokenKind.ELSE);
        assertEquals(kind.getKind(), TokenKind.ELSE);
    }

    @Test
    void fixedSourceGetSourceToken() {
        Token kind = new Token(TokenKind.ELSE);
        assertEquals(kind.getSource(), "else");
    }

    @Test
    void getKindToken() {
        Token kind = new Token(TokenKind.INTEGER, "1234");
        assertEquals(kind.getKind(), TokenKind.INTEGER);
    }

    @Test
    void getSourceToken() {
        Token kind = new Token(TokenKind.INTEGER, "1234");
        assertEquals(kind.getSource(), "1234");
    }

    @Test
    void failOnMissingFixedSourceToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Token(TokenKind.INTEGER);
        });
    }

    @Test
    void failOnMissingSourceToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Token(TokenKind.INTEGER, null);
        });
    }
}
