package daro.lang.ast;

import java.util.Objects;

/**
 * Class representing an ast node for a if-else condition. e.g. {@code if foo {
 * ... } else { ... }}
 * 
 * @author Roland Bernard
 */
public final class AstIfElse extends AstNode {
    private final AstNode condition;
    private final AstNode ifBlock;
    private final AstNode elseBlock;

    public AstIfElse(Position position, AstNode condition, AstNode ifBlock, AstNode elseBlock) {
        super(position);
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public AstNode getCondition() {
        return condition;
    }

    public AstNode getIf() {
        return ifBlock;
    }

    public AstNode getElse() {
        return elseBlock;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return (997 * condition.hashCode()) ^ (991 * ifBlock.hashCode()) ^ (877 * elseBlock.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstIfElse) {
            AstIfElse node = (AstIfElse)obj;
            return Objects.equals(condition, node.getCondition()) && Objects.equals(ifBlock, node.getIf())
                && Objects.equals(elseBlock, node.getElse());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + condition + " " + ifBlock + " " + elseBlock + ")";
    }
}
