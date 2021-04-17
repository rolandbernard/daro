package daro.lang.ast;

/**
 * Class representing an ast node for a less or equal comparison. e.g. {@code foo <= bar}
 * 
 * @author Roland Bernard
 */
public class AstLessOrEqual extends AstBinaryNode {

    public AstLessOrEqual(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

