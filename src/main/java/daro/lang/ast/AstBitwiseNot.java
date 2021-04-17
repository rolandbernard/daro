package daro.lang.ast;

public class AstBitwiseNot extends AstUnaryNode {

    public AstBitwiseNot(Position position, AstNode operator) {
        super(position, operator);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

