package daro.lang.ast;

/**
 * Class representing an ast node for a use statement. e.g. {@code use foo}
 * 
 * @author Roland Bernard
 */
public final class AstUse extends AstUnaryNode {

    public AstUse(Position position, AstNode operator) {
        super(position, operator);
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
