package daro.lang.ast;

/**
 * Class representing an ast node for a not equal comparison. e.g. {@code foo != bar}
 * 
 * @author Roland Bernard
 */
public class AstNotEqual extends AstBinaryNode {

    public AstNotEqual(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

