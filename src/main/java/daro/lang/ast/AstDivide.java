package daro.lang.ast;

/**
 * Class representing an ast node for a divition. e.g. {@code foo / bar}
 * 
 * @author Roland Bernard
 */
public class AstDivide extends AstBinaryNode {

    public AstDivide(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

