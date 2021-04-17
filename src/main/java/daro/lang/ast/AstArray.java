package daro.lang.ast;

/**
 * Class representing an ast node for array types. e.g. {@code []foo}
 * 
 * @author Roland Bernard
 */
public class AstArray extends AstUnaryNode {

    public AstArray(Position position, AstNode operator) {
        super(position, operator);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

