package daro.lang.ast;

import java.util.Objects;

/**
 * Abstract super class representing an ast node for a unary operation like
 * negation, boolean not, etc.
 * 
 * @author Roland Bernard
 */
public abstract class AstUnaryNode extends AstNode {
    private final AstNode operand;

    public AstUnaryNode(Position position, AstNode operand) {
        super(position);
        this.operand = operand;
    }

    public AstNode getOperand() {
        return operand;
    }

    @Override
    public AstNode[] getChildren() {
        return new AstNode[] {
            operand
        };
    }

    @Override
    public int hashCode() {
        return (997 * operand.hashCode()) ^ (877 * this.getClass().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            AstUnaryNode node = (AstUnaryNode)obj;
            return Objects.equals(operand, node.getOperand());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + operand + ")";
    }
}
