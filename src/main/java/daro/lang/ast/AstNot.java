package daro.lang.ast;

public class AstNot extends AstUnaryNode {

    public AstNot(Position position, AstNode operator) {
        super(position, operator);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

