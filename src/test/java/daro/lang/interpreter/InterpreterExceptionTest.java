package daro.lang.interpreter;

import org.junit.jupiter.api.Test;

import daro.lang.ast.Position;

import static org.junit.jupiter.api.Assertions.*;

public class InterpreterExceptionTest {

    @Test
    void getMessage() {
        Position position = new Position(40, 50);
        InterpreterException exception = new InterpreterException(position, "Error");
        assertEquals("Error", exception.getMessage());
    }

    @Test
    void getInterpreter() {
        Position position = new Position(40, 50);
        InterpreterException exception = new InterpreterException(position, "Error");
        assertSame(position, exception.getPosition());
    }
}
