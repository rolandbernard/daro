package daro.lang.interpreter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InterpreterExceptionTest {

    @Test
    void getMessage() {
        Interpreter interpreter = new Interpreter("");
        InterpreterException exception = new InterpreterException(interpreter, "Error");
        assertEquals("Error", exception.getMessage());
    }

    @Test
    void getInterpreter() {
        Interpreter interpreter = new Interpreter("");
        InterpreterException exception = new InterpreterException(interpreter, "Error");
        assertSame(interpreter, exception.getInterpreter());
    }
}
