package daro.lang.ast;

import java.util.Objects;

/**
 * Class representing an ast node for a member access. e.g. {@code foo.bar}
 * 
 * @author Roland Bernard
 */
public final class AstMember extends AstNode {
    private final AstNode operand;
    private final String name;

    public AstMember(Position position, AstNode operand, String name) {
        super(position);
        this.operand = operand;
        this.name = name;
    }

    public AstNode getOperand() {
        return operand;
    }

    public String getName() {
        return name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return (997 * operand.hashCode()) ^ (991 * name.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstMember) {
            AstMember node = (AstMember)obj;
            return Objects.equals(name, node.getName())
                && Objects.equals(operand, node.getOperand());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + operand + " " + name + ")";
    }
}

