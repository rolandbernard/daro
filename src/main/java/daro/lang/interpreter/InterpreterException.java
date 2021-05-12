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
     * @param position The {@link Position} that caused the exception
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

    /**
     * Return the starting offset of the position of the error.
     * @return The start of the error
     */
    public int getStart() {
        return position.getStart();
    }

    /**
     * Return the end offset of the position of the error.
     * @return The end of the error
     */
    public int getEnd() {
        return position.getEnd();
    }

    /**
     * Return the length offset of the position of the error.
     * @return The length of the error
     */
    public int getLength() {
        return position.getLength();
    }
}
