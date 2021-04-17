package daro.lang.ast;

/**
 * Class representing an ast node for a less than comparison. e.g. {@code foo < bar}
 * 
 * @author Roland Bernard
 */
public class AstLessThan extends AstBinaryNode {

    public AstLessThan(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

