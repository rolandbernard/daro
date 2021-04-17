package daro.lang.ast;

public class AstArray extends AstUnaryNode {

    public AstArray(Position position, AstNode operator) {
        super(position, operator);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

