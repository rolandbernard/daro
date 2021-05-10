package daro.lang.ast;

/**
 * Class representing an ast node for assignments. e.g. {@code foo = bar}
 * 
 * @author Roland Bernard
 */
public final class AstAssignment extends AstBinaryNode {

    public AstAssignment(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

