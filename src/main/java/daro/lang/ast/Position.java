package daro.lang.ast;

import java.nio.file.Path;

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
     * This variable stores the file in which the position lies
     */
    private final Path file;
    /**
     * The text the position refers to
     */
    private final String text;

    /**
     * Create a position which only specifies a {@link Path}. This should be uses if the source of the file can not be
     * read for example.
     * 
     * @param file
     *            The file the position is in
     */
    public Position(Path file) {
        this.start = -1;
        this.end = -1;
        this.text = null;
        this.file = file;
    }

    /**
     * Create a source {@link Position} from the start position.
     * 
     * @param start
     *            The starting position
     */
    public Position(int start) {
        this(start, start, null, null);
    }

    /**
     * Create a source {@link Position} from the start and end position.
     * 
     * @param start
     *            The starting position
     * @param end
     *            The end position
     */
    public Position(int start, int end) {
        this(start, end, null, null);
    }

    /**
     * Create a source {@link Position} from the start position and length.
     * 
     * @param start
     *            The starting position
     * @param end
     *            The end position
     * @param text
     *            The text the position is in
     */
    public Position(int start, int end, String text) {
        this(start, end, text, null);
    }

    /**
     * Create a source {@link Position} from the start position and length.
     * 
     * @param start
     *            The starting position
     * @param end
     *            The end position
     * @param text
     *            The text the position is in
     * @param file
     *            The file the position is in
     */
    public Position(int start, int end, String text, Path file) {
        if (start < 0) {
            throw new IllegalArgumentException("Position start must be non-negative");
        } else if (end < start) {
            throw new IllegalArgumentException("Position length must be non-negative");
        } else {
            this.start = start;
            this.end = end;
            this.text = text;
            this.file = file;
        }
    }

    /**
     * Create a new position that covers the area defined by the two extreme positions. This function can only be used
     * for positions that describe an exact position.
     * 
     * @param start
     *            The position to start at
     * @param end
     *            The position to end at
     */
    public Position(Position start, Position end) {
        this(start.getStart(), end.getEnd(), start.getText(), start.getFile());
    }

    /**
     * This method computes the line number from a offset into the given text.
     * 
     * @param offset
     *            The offset to check the line number of
     * @param text
     *            The text the offset refers to
     * 
     * @return The line number of the offset
     */
    public static int lineFromOffset(int offset, String text) {
        String[] lines = text.split("\n");
        int current = 0;
        int line = 0;
        while (line < lines.length && current <= offset) {
            current += lines[line].length() + 1;
            line++;
        }
        return line;
    }

    /**
     * This method computes the column number from a offset into the given text.
     * 
     * @param offset
     *            The offset to check the column number of
     * @param text
     *            The text the offset refers to
     * 
     * @return The column number of the offset
     */
    public static int columnFromOffset(int offset, String text) {
        String[] lines = text.split("\n");
        int current = 0;
        int line = 0;
        while (line < lines.length && current <= offset) {
            current += lines[line].length() + 1;
            line++;
        }
        line--;
        current -= lines[line].length() + 1;
        return offset - current + 1;
    }

    /**
     * Returns the starting position of the source code {@link Position}. The value will be negative if no exact
     * position is associated with the position.
     * 
     * @return The start position
     */
    public int getStart() {
        return start;
    }

    /**
     * Returns the ending position (exclusive) of the source code {@link Position}. The value will be negative if no
     * exact position is associated with the position.
     * 
     * @return The end position
     */
    public int getEnd() {
        return end;
    }

    /**
     * Returns the length of the source code {@link Position}
     * 
     * @return The length position
     */
    public int getLength() {
        return end - start;
    }

    /**
     * Returns the text the {@link Position} refers to. This could be null.
     * 
     * @return The text of the position
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the file the {@link Position} refers to. This could be null.
     * 
     * @return The file of the position
     */
    public Path getFile() {
        return file;
    }

    @Override
    public int hashCode() {
        return (997 * Integer.hashCode(start)) ^ (991 * Integer.hashCode(end));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position position = (Position) obj;
            return start == position.getStart() && end == position.getEnd() && text == position.getText()
                    && file == position.getFile();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (start == -1) {
            return file.toString();
        } else if (text == null) {
            String ret = "";
            if (file != null) {
                ret += file.toString() + ":";
            }
            ret += "(" + start + "," + getLength() + ")";
            return ret;
        } else {
            String ret = "";
            if (file != null) {
                ret += file.toString() + ":";
            }
            ret += lineFromOffset(start, text) + ":" + columnFromOffset(start, text);
            if (start != end) {
                ret += " - " + lineFromOffset(end, text) + ":" + columnFromOffset(end, text);
            }
            return ret;
        }
    }
}
