package daro.lang.ast;

/**
 * Class representing an ast node for a multiplication. e.g. {@code foo * bar}
 * 
 * @author Roland Bernard
 */
public final class AstMultiply extends AstBinaryNode {

    public AstMultiply(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

