package daro.lang.values;

import daro.lang.interpreter.EmptyScope;
import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents a string value.
 * 
 * @author Roland Bernard
 */
public class UserString extends UserObject {
    private final String value;

    public UserString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public UserType getType() {
        return new UserTypeString();
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
        if (object instanceof UserString) {
            UserString integer = (UserString)object;
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
