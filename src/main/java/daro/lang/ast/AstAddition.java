package daro.lang.ast;

public class AstAddition extends AstBinaryNode {

    public AstAddition(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

