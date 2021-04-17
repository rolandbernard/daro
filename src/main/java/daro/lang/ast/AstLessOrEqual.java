package daro.lang.ast;

public class AstLessOrEqual extends AstBinaryNode {

    public AstLessOrEqual(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

