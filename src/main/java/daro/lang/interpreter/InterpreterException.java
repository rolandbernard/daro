package daro.lang.interpreter;

import daro.lang.ast.Position;

/**
 * This class represents an error thrown during the interpreter execution. It
 * includes a reference to the position where the exception was thrown.
 *
 * @author Roland Bernard
 */
public class InterpreterException extends DaroException {
    private static final long serialVersionUID = 1L;

    /**
     * Create a {@link InterpreterException} for the given message and without a
     * position.
     * 
     * @param message The message for the exception
     */
    public InterpreterException(String message) {
        super(message);
    }

    /**
     * Create a {@link InterpreterException} for the given position and message.
     * 
     * @param position The {@link Position} that caused the exception
     * @param message  The message for the exception
     */
    public InterpreterException(Position position, String message) {
        super(position, message);
    }

    @Override
    public String toString() {
        return "Runtime error at " + getPosition() + ": " + getMessage();
    }
}
