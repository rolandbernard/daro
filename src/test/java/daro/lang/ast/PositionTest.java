package daro.lang.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    void getStartPosition() {
        Position position = new Position(42, 100);
        assertEquals(42, position.getStart());
    }

    @Test
    void getEndPosition() {
        Position position = new Position(00, 100);
        assertEquals(100, position.getEnd());
    }

    @Test
    void getLengthPosition() {
        Position position = new Position(42, 100);
        assertEquals(58, position.getLength());
    }

    @Test
    void hashCodePosition() {
        Position position1 = new Position(42, 100);
        Position position2 = new Position(42, 100);
        assertEquals(position1.hashCode(), position2.hashCode());
    }

    @Test
    void equalsPosition() {
        Position position1 = new Position(42, 100);
        Position position2 = new Position(42, 100);
        assertEquals(position1, position1);
        assertEquals(position1, position2);
    }

    @Test
    void notEqualsPosition() {
        Position position1 = new Position(42, 100);
        Position position2 = new Position(0, 100);
        Position position3 = new Position(42, 101);
        assertNotEquals(position1, position2);
        assertNotEquals(position1, position3);
        assertNotEquals(position2, new Object());
    }

    @Test
    void toStringPosition() {
        Position position1 = new Position(42, 100);
        assertEquals("(42,58)", position1.toString());
    }

    @Test
    void invalidStartPosition() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Position(-42, 100);
        });
    }

    @Test
    void invalidLengthPosition() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Position(42, -100);
        });
    }
}
