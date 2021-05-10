package daro.lang.interpreter;

import daro.lang.ast.Position;

/**
 * This class represents an error thrown during the interpreter execution. It includes a reference
 * to the position where the exception was thrown.
 *
 * @author Roland Bernard
 */
public class InterpreterException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private final Position position;

    /**
     * Create a {@link InterpreterException} for the given position and message.
     * @param interpreter The {@link Position} that caused the exception
     * @param message The message for the exception
     */
    public InterpreterException(Position position, String message) {
        super(message);
        this.position = position;
    }

    /**
     * Returns the position the exception applies to.
     * @return The position that caused the exception
     */
    public Position getPosition() {
        return position;
    }
}
