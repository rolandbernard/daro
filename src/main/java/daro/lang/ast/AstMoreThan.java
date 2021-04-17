package daro.lang.ast;

public class AstMoreThan extends AstBinaryNode {

    public AstMoreThan(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

