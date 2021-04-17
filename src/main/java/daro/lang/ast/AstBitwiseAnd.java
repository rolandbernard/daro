package daro.lang.ast;

public class AstBitwiseAnd extends AstBinaryNode {

    public AstBitwiseAnd(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

