package daro.lang.ast;

/**
 * Class representing an ast node for a integer literal. e.g. {@code 42}
 * 
 * @author Roland Bernard
 */
public final class AstInteger extends AstNode {
    private final long value;

    public AstInteger(Position position, long value) {
        super(position);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstInteger) {
            AstInteger node = (AstInteger)obj;
            return value == node.getValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

