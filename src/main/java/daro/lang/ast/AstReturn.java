package daro.lang.ast;

/**
 * Class representing an ast node for a return. e.g. {@code return foo}
 * 
 * @author Roland Bernard
 */
public final class AstReturn extends AstUnaryNode {

    public AstReturn(Position position, AstNode operator) {
        super(position, operator);
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

