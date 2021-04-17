package daro.lang.ast;

public class AstNotEqual extends AstBinaryNode {

    public AstNotEqual(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

