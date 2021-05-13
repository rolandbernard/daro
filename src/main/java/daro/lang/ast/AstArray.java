package daro.lang.ast;

/**
 * Class representing an ast node for array types. e.g. {@code [5]foo}
 * 
 * @author Roland Bernard
 */
public final class AstArray extends AstBinaryNode {

    public AstArray(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

