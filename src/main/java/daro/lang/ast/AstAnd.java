package daro.lang.ast;

public class AstAnd extends AstBinaryNode {

    public AstAnd(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

