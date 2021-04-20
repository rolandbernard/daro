package daro.lang.ast;

/**
 * Class representing an ast node for a boolean not. e.g. {@code !foo}
 * 
 * @author Roland Bernard
 */
public final class AstNot extends AstUnaryNode {

    public AstNot(Position position, AstNode operator) {
        super(position, operator);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

