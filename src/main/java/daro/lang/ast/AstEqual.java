package daro.lang.ast;

/**
 * Class representing an ast node for a equal comparison. e.g. {@code foo == bar}
 * 
 * @author Roland Bernard
 */
public final class AstEqual extends AstBinaryNode {

    public AstEqual(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

