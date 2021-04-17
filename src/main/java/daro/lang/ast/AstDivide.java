package daro.lang.ast;

public class AstDivide extends AstBinaryNode {

    public AstDivide(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

