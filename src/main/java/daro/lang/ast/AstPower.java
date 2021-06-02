package daro.lang.ast;

/**
 * Class representing an ast node for a power. e.g. {@code foo ** bar}
 *
 * @author Daniel Planötscher
 */
public final class AstPower extends AstBinaryNode {

    public AstPower(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
