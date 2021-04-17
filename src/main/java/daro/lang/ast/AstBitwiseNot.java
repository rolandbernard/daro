package daro.lang.ast;

/**
 * Class representing an ast node for bitwise not. e.g. {@code ~bar}
 * 
 * @author Roland Bernard
 */
public class AstBitwiseNot extends AstUnaryNode {

    public AstBitwiseNot(Position position, AstNode operator) {
        super(position, operator);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

