package daro.lang.ast;

/**
 * Class representing an ast node for array types. e.g. {@code []foo}
 * 
 * @author Roland Bernard
 */
public final class AstArray extends AstUnaryNode {

    public AstArray(Position position, AstNode operator) {
        super(position, operator);
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

