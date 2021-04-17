package daro.lang.ast;

public abstract class AstIndex extends AstBinaryNode {

    public AstIndex(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

