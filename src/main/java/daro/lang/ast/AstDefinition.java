package daro.lang.ast;

import java.util.Objects;

/**
 * Class representing an ast node for a variable definition. e.g. {@code foo: bar = fizz}
 * 
 * @author Roland Bernard
 */
public final class AstDefinition extends AstNode {
    private final String name;
    private final AstNode type;
    private final AstNode value;

    public AstDefinition(Position position, String name, AstNode type, AstNode value) {
        super(position);
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public AstNode getType() {
        return type;
    }

    public AstNode getValue() {
        return value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return (997 * name.hashCode()) ^ (991 * type.hashCode()) ^ (877 * value.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstDefinition) {
            AstDefinition node = (AstDefinition)obj;
            return Objects.equals(name, node.getName())
                && Objects.equals(type, node.getType())
                && Objects.equals(value, node.getValue());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + name + " " + type + " " + value + ")";
    }
}

