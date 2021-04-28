package daro.lang.parser;

import daro.lang.ast.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParsingExceptionTest {

    @Test
    void getMessage() {
        ParsingException exception = new ParsingException(new Position(10, 15), "Error");
        assertEquals("Error", exception.getMessage());
    }

    @Test
    void getPosition() {
        ParsingException exception = new ParsingException(new Position(10, 15), "Error");
        assertEquals(new Position(10, 15), exception.getPosition());
    }

    @Test
    void getStart() {
        ParsingException exception = new ParsingException(new Position(10, 15), "Error");
        assertEquals(10, exception.getStart());
    }

    @Test
    void getEnd() {
        ParsingException exception = new ParsingException(new Position(10, 15), "Error");
        assertEquals(15, exception.getEnd());
    }

    @Test
    void getLength() {
        ParsingException exception = new ParsingException(new Position(10, 15), "Error");
        assertEquals(5, exception.getLength());
    }
}

