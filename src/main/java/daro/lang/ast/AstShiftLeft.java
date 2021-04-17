package daro.lang.ast;

/**
 * Class representing an ast node for a shift left. e.g. {@code foo << bar}
 * 
 * @author Roland Bernard
 */
public class AstShiftLeft extends AstBinaryNode {

    public AstShiftLeft(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

