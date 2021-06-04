package daro.lang.interpreter;

import java.nio.file.Path;

import daro.lang.ast.Position;

/**
 * This class represents an error thrown during the interpreter execution. It
 * includes a reference to the position where the exception was thrown.
 *
 * @author Roland Bernard
 */
public class DaroException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private final Position position;

    /**
     * Create a {@link DaroException} for the given message and without a
     * position.
     * 
     * @param message The message for the exception
     */
    public DaroException(String message) {
        this(null, message);
    }

    /**
     * Create a {@link DaroException} for the given position and message.
     * 
     * @param position The {@link Position} that caused the exception
     * @param message  The message for the exception
     */
    public DaroException(Position position, String message) {
        super(message);
        this.position = position;
    }

    /**
     * Returns the position the exception applies to.
     * 
     * @return The position that caused the exception
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Return the starting offset of the position of the error.
     * 
     * @return The start of the error
     */
    public int getStart() {
        return position.getStart();
    }

    /**
     * Return the end offset of the position of the error.
     * 
     * @return The end of the error
     */
    public int getEnd() {
        return position.getEnd();
    }

    /**
     * Return the file of the position of the error.
     * 
     * @return The file of the error
     */
    public Path getFile() {
        return position.getFile();
    }

    /**
     * Return the length offset of the position of the error.
     * 
     * @return The length of the error
     */
    public int getLength() {
        return position.getLength();
    }
}
