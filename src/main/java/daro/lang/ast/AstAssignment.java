package daro.lang.ast;

/**
 * Class representing an ast node for assignments. e.g. {@code foo = bar}
 * 
 * @author Roland Bernard
 */
public abstract class AstAssignment extends AstBinaryNode {

    public AstAssignment(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

