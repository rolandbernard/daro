package daro.lang.ast;

public class AstMoreOrEqual extends AstBinaryNode {

    public AstMoreOrEqual(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

