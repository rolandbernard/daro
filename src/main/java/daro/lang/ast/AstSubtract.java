package daro.lang.ast;

/**
 * Class representing an ast node for a subtraction. e.g. {@code foo - bar}
 * 
 * @author Roland Bernard
 */
public final class AstSubtract extends AstBinaryNode {

    public AstSubtract(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

