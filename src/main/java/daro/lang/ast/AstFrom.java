package daro.lang.ast;

/**
 * Class representing an ast node for a from statement. e.g. {@code from "test.daro"}
 * 
 * @author Roland Bernard
 */
public final class AstFrom extends AstUnaryNode {

    public AstFrom(Position position, AstNode operator) {
        super(position, operator);
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
