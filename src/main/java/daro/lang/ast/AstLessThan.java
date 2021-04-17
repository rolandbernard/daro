package daro.lang.ast;

public class AstLessThan extends AstBinaryNode {

    public AstLessThan(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

