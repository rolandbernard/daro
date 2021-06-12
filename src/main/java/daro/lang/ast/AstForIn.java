package daro.lang.ast;

import java.util.Objects;

/**
 * Class representing an ast node for a for-in loop. e.g. {@code for foo in bar
 * { ... }}
 * 
 * @author Roland Bernard
 */
public final class AstForIn extends AstNode {
    private final AstSymbol variable;
    private final AstNode list;
    private final AstNode body;

    public AstForIn(Position position, AstSymbol variable, AstNode list, AstNode body) {
        super(position);
        this.variable = variable;
        this.list = list;
        this.body = body;
    }

    public AstSymbol getVariable() {
        return variable;
    }

    public AstNode getList() {
        return list;
    }

    public AstNode getBody() {
        return body;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AstNode[] getChildren() {
        return new AstNode[] {
            variable, list, body
        };
    }

    @Override
    public int hashCode() {
        return (997 * variable.hashCode()) ^ (991 * list.hashCode()) ^ (877 * body.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstForIn) {
            AstForIn node = (AstForIn)obj;
            return Objects.equals(variable, node.getVariable()) && Objects.equals(list, node.getList())
                && Objects.equals(body, node.getBody());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + variable + " " + list + " " + body + ")";
    }
}
