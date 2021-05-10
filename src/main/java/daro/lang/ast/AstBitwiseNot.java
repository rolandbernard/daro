package daro.lang.ast;

/**
 * Class representing an ast node for bitwise not. e.g. {@code ~bar}
 * 
 * @author Roland Bernard
 */
public final class AstBitwiseNot extends AstUnaryNode {

    public AstBitwiseNot(Position position, AstNode operator) {
        super(position, operator);
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

