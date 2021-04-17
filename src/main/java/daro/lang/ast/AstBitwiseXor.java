package daro.lang.ast;

public class AstBitwiseXor extends AstBinaryNode {

    public AstBitwiseXor(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

