package daro.lang.ast;

import java.util.Objects;

/**
 * Class representing an ast node for a new operation. e.g. {@code new foo{ ...
 * }}
 * 
 * @author Roland Bernard
 */
public final class AstNew extends AstNode {
    private final AstNode type;
    private final AstInitializer initializer;

    public AstNew(Position position, AstNode type, AstInitializer initializer) {
        super(position);
        this.type = type;
        this.initializer = initializer;
    }

    public AstNode getType() {
        return type;
    }

    public AstInitializer getInitializer() {
        return initializer;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AstNode[] getChildren() {
        return new AstNode[] {
            type, initializer
        };
    }

    @Override
    public int hashCode() {
        return (997 * type.hashCode()) ^ (991 * initializer.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstNew) {
            AstNew node = (AstNew)obj;
            return Objects.equals(type, node.getType()) && Objects.equals(initializer, node.getInitializer());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + type + " " + initializer + ")";
    }
}
