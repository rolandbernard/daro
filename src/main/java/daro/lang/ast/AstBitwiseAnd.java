package daro.lang.ast;

/**
 * Class representing an ast node for bitwise and. e.g. {@code foo & bar}
 * 
 * @author Roland Bernard
 */
public final class AstBitwiseAnd extends AstBinaryNode {

    public AstBitwiseAnd(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

