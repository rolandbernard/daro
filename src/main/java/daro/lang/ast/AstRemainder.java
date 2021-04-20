package daro.lang.ast;

/**
 * Class representing an ast node for a remainder. e.g. {@code foo % bar}
 * 
 * @author Roland Bernard
 */
public final class AstRemainder extends AstBinaryNode {

    public AstRemainder(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

