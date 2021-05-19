package daro.lang.parser;

import daro.lang.ast.Position;

/**
 * This class is used to hold errors generated by the {@link Parser} during the parsing process.
 *
 * @author Roland Bernard
 */
public class ParsingException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private final Position position;

    /**
     * Creates a new parser error for the given position and with the given message.
     * 
     * @param position
     *            The position of the error
     * @param message
     *            The message of the message
     */
    public ParsingException(Position position, String message) {
        super(message);
        this.position = position;
    }

    /**
     * Get the position of the error.
     * 
     * @return The position of the error
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
     * Return the length offset of the position of the error.
     * 
     * @return The length of the error
     */
    public int getLength() {
        return position.getLength();
    }
}
