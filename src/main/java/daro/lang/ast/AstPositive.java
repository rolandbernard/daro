package daro.lang.ast;

/**
 * Class representing an ast node for a unary plus. e.g. {@code +foo}
 * 
 * @author Roland Bernard
 */
public class AstPositive extends AstUnaryNode {

    public AstPositive(Position position, AstNode operator) {
        super(position, operator);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

