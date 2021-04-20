package daro.lang.ast;

import java.util.Objects;

/**
 * Class representing an ast node for a string literal. e.g. {@code "foo"}
 * 
 * @author Roland Bernard
 */
public final class AstString extends AstNode {
    private final String value;

    public AstString(Position position, String value) {
        super(position);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstString) {
            AstString node = (AstString)obj;
            return Objects.equals(value, node.getValue());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}

