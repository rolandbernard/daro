package daro.lang.ast;

public class AstPositive extends AstUnaryNode {

    public AstPositive(Position position, AstNode operator) {
        super(position, operator);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

