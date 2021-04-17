package daro.lang.ast;

public class AstReturn extends AstUnaryNode {

    public AstReturn(Position position, AstNode operator) {
        super(position, operator);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

