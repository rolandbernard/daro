package daro.lang.ast;

/**
 * Class representing an ast node for addition. e.g. {@code foo + bar}
 * 
 * @author Roland Bernard
 */
public class AstAddition extends AstBinaryNode {

    public AstAddition(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

