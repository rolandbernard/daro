package daro.lang.ast;

public class AstShiftLeft extends AstBinaryNode {

    public AstShiftLeft(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

