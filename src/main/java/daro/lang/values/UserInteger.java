package daro.lang.values;

import java.math.BigInteger;

import daro.lang.interpreter.EmptyScope;
import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents an integer value.
 * 
 * @author Roland Bernard
 */
public class UserInteger extends UserObject {
    private final BigInteger value;

    public UserInteger(BigInteger value) {
        this.value = value;
    }

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
}
