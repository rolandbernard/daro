package daro.lang.ast;

public class AstBitwiseOr extends AstBinaryNode {

    public AstBitwiseOr(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

