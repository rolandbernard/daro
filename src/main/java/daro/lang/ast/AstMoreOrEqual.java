package daro.lang.ast;

/**
 * Class representing an ast node for a more or equal comparison. e.g. {@code foo >= bar}
 * 
 * @author Roland Bernard
 */
public class AstMoreOrEqual extends AstBinaryNode {

    public AstMoreOrEqual(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

