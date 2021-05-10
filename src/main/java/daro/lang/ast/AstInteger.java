package daro.lang.ast;

import java.math.BigInteger;

/**
 * Class representing an ast node for a integer literal. e.g. {@code 42}
 * 
 * @author Roland Bernard
 */
public final class AstInteger extends AstNode {
    private final BigInteger value;

    public AstInteger(Position position, BigInteger value) {
        super(position);
        this.value = value;
    }

    public AstInteger(Position position, long value) {
        this(position, BigInteger.valueOf(value));
    }

    public BigInteger getValue() {
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
        if (obj instanceof AstInteger) {
            AstInteger node = (AstInteger)obj;
            return value.equals(node.getValue());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

