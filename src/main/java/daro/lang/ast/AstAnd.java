package daro.lang.ast;

/**
 * Class representing an ast node for boolean and. e.g. {@code foo && bar}
 * 
 * @author Roland Bernard
 */
public final class AstAnd extends AstBinaryNode {

    public AstAnd(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

