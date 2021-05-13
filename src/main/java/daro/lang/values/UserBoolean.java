package daro.lang.values;

import daro.lang.interpreter.EmptyScope;
import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents a boolean value.
 * 
 * @author Roland Bernard
 */
public class UserBoolean extends UserObject {
    private final boolean value;

    /**
     * Create a new boolean value from the given value.
     * @param value The value the {@link UserBoolean} represents
     */
    public UserBoolean(boolean value) {
        this.value = value;
    }

    /**
     * Returns the value of this boolean.
     * @return The value the {@link UserBoolean} represents
     */
    public boolean getValue() {
        return value;
    }

    @Override
    public UserType getType() {
        return new UserTypeBoolean();
    }

    @Override
    public Scope getMemberScope() {
        // TODO: add methods
        return new EmptyScope();
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserBoolean) {
            UserBoolean bool = (UserBoolean)object;
            return value == bool.getValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean isTrue() {
        return value;
    }
}
