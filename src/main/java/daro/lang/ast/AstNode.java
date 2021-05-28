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
     * 
     * @param position
     *            Source position represented by the {@link AstNode}
     */
    public AstNode(Position position) {
        this.position = position;
    }

    /**
     * Returns the source position that is represented by the {@link AstNode}.
     * 
     * @return The source position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Accepts a ast {@link Visitor} calling a method debending on the base class implementation.
     * 
     * @param <T>
     *            The type returned by the visitor
     * @param visitor
     *            The visitor to accept
     * 
     * @return The result from the visitor
     */
    abstract public <T> T accept(Visitor<T> visitor);
}
