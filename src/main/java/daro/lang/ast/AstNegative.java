package daro.lang.ast;

/**
 * Class representing an ast node for a negation. e.g. {@code -foo}
 * 
 * @author Roland Bernard
 */
public final class AstNegative extends AstUnaryNode {

    public AstNegative(Position position, AstNode operator) {
        super(position, operator);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

