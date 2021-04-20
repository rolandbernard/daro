package daro.lang.ast;

import java.util.Objects;

/**
 * Class representing an ast node for a for loop. e.g. {@code for foo { ... }}
 * 
 * @author Roland Bernard
 */
public final class AstFor extends AstNode {
    private final AstNode condition;
    private final AstNode body;

    public AstFor(Position position, AstNode condition, AstNode body) {
        super(position);
        this.condition = condition;
        this.body = body;
    }

    public AstNode getCondition() {
        return condition;
    }

    public AstNode getBody() {
        return body;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return (997 * condition.hashCode()) ^ (991 * body.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstFor) {
            AstFor node = (AstFor)obj;
            return Objects.equals(condition, node.getCondition())
                && Objects.equals(body, node.getBody());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + condition + " " + body + ")";
    }
}

