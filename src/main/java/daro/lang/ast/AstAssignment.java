package daro.lang.ast;

public abstract class AstAssignment extends AstBinaryNode {

    public AstAssignment(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

