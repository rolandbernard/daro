package daro.lang.ast;

/**
 * Class representing an ast node for a less or equal comparison. e.g. {@code foo <= bar}
 * 
 * @author Roland Bernard
 */
public final class AstLessOrEqual extends AstBinaryNode {

    public AstLessOrEqual(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

