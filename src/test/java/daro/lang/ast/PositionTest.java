package daro.lang.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

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
    void toStringWithoutText() {
        Position position1 = new Position(42, 100);
        assertEquals("42..100", position1.toString());
    }

    @Test
    void toStringWithoutFile() {
        Position position1 = new Position(1, 5, "\nTest\n");
        assertEquals("2:1..2:4", position1.toString());
    }

    @Test
    void toStringWithFile() {
        Position position1 = new Position(1, 5, "\nTest\n", Path.of("test.txt"));
        assertEquals("test.txt:2:1..2:4", position1.toString());
    }

    @Test
    void toStringSingleChar() {
        Position position1 = new Position(1, 2, "\nTest\n");
        assertEquals("2:1", position1.toString());
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

   @Test
   void lineNumber() {
      assertEquals(3, Position.lineFromOffset(10, "0123\n567\n90123\n567"));
   }

   @Test
   void columnNumber() {
      assertEquals(2, Position.columnFromOffset(10, "0123\n567\n90123\n567"));
   }

   @Test
   void lineNumberInEmptyText() {
      assertEquals(1, Position.lineFromOffset(10, ""));
   }

   @Test
   void columnNumberInEmptyText() {
      assertEquals(11, Position.columnFromOffset(10, ""));
   }

   @Test
   void lineNumberAtEnd() {
      assertEquals(3, Position.lineFromOffset(14, "0123\n567\n90123\n"));
   }

   @Test
   void columnNumberAtEnd() {
      assertEquals(6, Position.columnFromOffset(14, "0123\n567\n90123\n"));
   }
}
