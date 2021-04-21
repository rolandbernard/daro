package daro.lang.ast;

/**
 * Abstract super class for all classes representing a node in the ast.
 *
 * @author Roland Bernard
 */
public abstract class AstNode {
    private final Position position;

    /**
     * Construct a {@link AstNode} for the given position.
     * @param position Source position represented by the {@link AstNode}
     */
    public AstNode(Position position) {
        this.position = position;
    }

    /**
     * Returns the source position that is represented by the {@link AstNode}.
     * @return The source position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the start of the source position.
     * @return Start of the source position
     */
    public int getStart() {
        return position.getStart();
    }

    /**
     * Returns the end of the source position.
     * @return End of the source position
     */
    public int getEnd() {
        return position.getEnd();
    }

    /**
     * Returns the length of the source position.
     * @return Length of the source position
     */
    public int getLength() {
        return position.getLength();
    }

    /**
     * Accepts a ast {@link Visitor} calling a method debending on the base class implementation.
     * @param visitor The visitor to accept
     */
    abstract public void accept(Visitor visitor);
}

