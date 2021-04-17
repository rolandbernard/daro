package daro.lang.ast;

public class AstRemainder extends AstBinaryNode {

    public AstRemainder(Position position, AstNode left, AstNode right) {
        super(position, left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

