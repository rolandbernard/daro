package daro.lang.ast;

public abstract class AstUnaryNode extends AstNode {
    private final AstNode operator;

    public AstUnaryNode(Position position, AstNode operator) {
        super(position);
        this.operator = operator;
    }

    public AstNode getOperator() {
        return operator;
    }
}

