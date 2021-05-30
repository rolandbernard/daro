package daro.lang.ast;

import java.util.Objects;

/**
 * Class representing an ast node for a class. e.g. {@code class foo { ... }}
 * 
 * @author Roland Bernard
 */
public final class AstClass extends AstNode {
    private final String name;
    private final AstBlock body;

    public AstClass(Position position, String name, AstBlock body) {
        super(position);
        this.name = name;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public AstBlock getBody() {
        return body;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return (997 * name.hashCode()) ^ (991 * body.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstClass) {
            AstClass node = (AstClass)obj;
            return Objects.equals(name, node.getName()) && Objects.equals(body, node.getBody());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + name + " " + body + ")";
    }
}
