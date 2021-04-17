package daro.lang.ast;

/**
 * Class representing the source code position of a {@link AstNode}.
 *
 * @author Roland Bernard
 */
public class Position {
    /**
     * This variable stores the start of the source position.
     */
    private final int start;
    /**
     * This variable stores the end (exclusive) of the source position.
     */
    private final int end;

    /**
     * Create a source {@link Position} from the start position and length.
     * @param start The starting position
     * @param length The length
     */
    public Position(int start, int length) {
        this.start = start;
        this.end = start + length;
    }

    /**
     * Returns the starting position of the source code {@link Position}
     * @return The start position
     */
    int getStart() {
        return start;
    }

    /**
     * Returns the ending position (exclusive) of the source code {@link Position}
     * @return The end position
     */
    int getEnd() {
        return end;
    }

    /**
     * Returns the length of the source code {@link Position}
     * @return The length position
     */
    int getLength() {
        return end - start;
    }
}
