package daro.lang.ast;

/**
 * Class representing an ast node for a real literal. e.g. {@code 42.17e29}
 * 
 * @author Roland Bernard
 */
public final class AstReal extends AstNode {
    private final double value;

    public AstReal(Position position, double value) {
        super(position);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstReal) {
            AstReal node = (AstReal)obj;
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

