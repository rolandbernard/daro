package daro.lang.ast;

/**
 * Class representing an ast node for a more than comparison. e.g. {@code foo > bar}
 * 
 * @author Roland Bernard
 */
public final class AstMoreThan extends AstBinaryNode {

    public AstMoreThan(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

