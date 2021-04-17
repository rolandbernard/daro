package daro.lang.ast;

public class AstMultiply extends AstBinaryNode {

    public AstMultiply(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

