
package daro.lang.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenKindTest {

    @Test
    void hasFixedSourceTokenKind() {
        TokenKind kind = TokenKind.ELSE;
        assertTrue(kind.hasFixedSource());
    }

    @Test
    void getFixedSourceTokenKind() {
        TokenKind kind = TokenKind.ELSE;
        assertEquals("else", kind.getFixedSource());
    }

    @Test
    void doesNotHaveFixedSourceTokenKind() {
        TokenKind kind = TokenKind.INTEGER;
        assertFalse(kind.hasFixedSource());
    }

    @Test
    void canNotGetFixedSourceTokenKind() {
        TokenKind kind = TokenKind.INTEGER;
        assertThrows(IllegalArgumentException.class, () -> {
            kind.getFixedSource();
        });
    }

    @Test
    void findFixedSourceTokenKind() {
        TokenKind kind = TokenKind.findForFixedSource("else");
        assertEquals(TokenKind.ELSE, kind);
    }

    @Test
    void canNotFindFixedSourceTokenKind() {
        TokenKind kind = TokenKind.findForFixedSource("main");
        assertNull(kind);
    }
}
