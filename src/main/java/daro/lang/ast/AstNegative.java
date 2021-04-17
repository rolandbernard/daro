package daro.lang.ast;

public class AstNegative extends AstUnaryNode {

    public AstNegative(Position position, AstNode operator) {
        super(position, operator);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

