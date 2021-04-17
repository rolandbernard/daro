package daro.lang.ast;

public class AstEqual extends AstBinaryNode {

    public AstEqual(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

