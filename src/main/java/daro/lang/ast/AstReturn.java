package daro.lang.ast;

/**
 * Class representing an ast node for a return. e.g. {@code return foo}
 * 
 * @author Roland Bernard
 */
public class AstReturn extends AstUnaryNode {

    public AstReturn(Position position, AstNode operator) {
        super(position, operator);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

