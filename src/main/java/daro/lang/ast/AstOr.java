package daro.lang.ast;

public class AstOr extends AstBinaryNode {

    public AstOr(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

