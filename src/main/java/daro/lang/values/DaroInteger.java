package daro.lang.values;

import java.math.BigInteger;

/**
 * This {@link DaroObject} represents an integer value.
 * 
 * @author Roland Bernard
 */
public class DaroInteger extends DaroNumber {
    private final BigInteger value;

    /**
     * Create a new {@link DaroInteger} from the given value.
     * 
     * @param value The value to be stored in the {@link DaroInteger}
     */
    public DaroInteger(BigInteger value) {
        this.value = value;
    }

    /**
     * Returns the value stored inside the {@link DaroInteger}
     * 
     * @return The value stored in this object
     */
    public BigInteger getValue() {
        return value;
    }

    @Override
    public DaroType getType() {
        return new DaroTypeInteger();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroInteger) {
            DaroInteger integer = (DaroInteger)object;
            return value.equals(integer.getValue());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public boolean isTrue() {
        return !value.equals(BigInteger.ZERO);
    }
}
