package daro.lang.ast;

/**
 * Class representing an ast node for bitwise or. e.g. {@code foo | bar}
 * 
 * @author Roland Bernard
 */
public class AstBitwiseOr extends AstBinaryNode {

    public AstBitwiseOr(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

