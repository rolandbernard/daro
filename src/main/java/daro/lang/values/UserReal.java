package daro.lang.values;

import daro.lang.interpreter.EmptyScope;
import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents a real value.
 * 
 * @author Roland Bernard
 */
public class UserReal extends UserNumber {
    private final double value;

    public UserReal(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public UserType getType() {
        return new UserTypeReal();
    }

    @Override
    public Scope getMemberScope() {
        // TODO: add methods
        return new EmptyScope();
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserReal) {
            UserReal real = (UserReal)object;
            return value == real.getValue();
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
        return value;
    }

    @Override
    public boolean isTrue() {
        return value != 0.0;
    }
}
