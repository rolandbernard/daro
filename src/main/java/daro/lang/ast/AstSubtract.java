package daro.lang.ast;

public class AstSubtract extends AstBinaryNode {

    public AstSubtract(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

