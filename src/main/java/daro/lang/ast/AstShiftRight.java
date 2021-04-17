package daro.lang.ast;

public class AstShiftRight extends AstBinaryNode {

    public AstShiftRight(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

