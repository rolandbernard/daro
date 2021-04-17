package daro.lang.ast;

/**
 * Abstract super class representing an ast node for a unary opreation like negation, boolean not, etc.
 * 
 * @author Roland Bernard
 */
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

