package daro.lang.ast;

/**
 * Class representing an ast node for a shift right. e.g. {@code foo >> bar}
 * 
 * @author Roland Bernard
 */
public final class AstShiftRight extends AstBinaryNode {

    public AstShiftRight(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

