package daro.lang.values;

import java.math.BigInteger;

import daro.lang.interpreter.EmptyScope;
import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents an integer value.
 * 
 * @author Roland Bernard
 */
public class UserInteger extends UserNumber {
    private final BigInteger value;

    /**
     * Create a new {@link UserInteger} from the given value.
     * @param value The value to be stored in the {@link UserInteger}
     */
    public UserInteger(BigInteger value) {
        this.value = value;
    }

    /**
     * Returns the value stored inside the {@link UserInteger}
     * @return The value stored in this object
     */
    public BigInteger getValue() {
        return value;
    }

    @Override
    public UserType getType() {
        return new UserTypeInteger();
    }

    @Override
    public Scope getMemberScope() {
        // TODO: add methods
        return new EmptyScope();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserInteger) {
            UserInteger integer = (UserInteger)object;
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
