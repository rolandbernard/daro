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
    public Position(int start, int end) {
        if (start < 0) {
            throw new IllegalArgumentException("Position start must be non-negative");
        } else if (end < start) {
            throw new IllegalArgumentException("Position length must be non-negative");
        } else {
            this.start = start;
            this.end = end;
        }
    }

    /**
     * Returns the starting position of the source code {@link Position}
     * @return The start position
     */
    public int getStart() {
        return start;
    }

    /**
     * Returns the ending position (exclusive) of the source code {@link Position}
     * @return The end position
     */
    public int getEnd() {
        return end;
    }

    /**
     * Returns the length of the source code {@link Position}
     * @return The length position
     */
    public int getLength() {
        return end - start;
    }

    @Override
    public int hashCode() {
        return (997 * start) ^ (991 * end);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position position = (Position)obj;
            return (start == position.getStart() && end == position.getEnd());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + start + "," + getLength() + ")";
    }
}
