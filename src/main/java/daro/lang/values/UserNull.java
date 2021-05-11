package daro.lang.values;

import daro.lang.interpreter.EmptyScope;
import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents a null value.
 * 
 * @author Roland Bernard
 */
public class UserNull extends UserObject {

    @Override
    public UserType getType() {
        return new UserTypeNull();
    }

    @Override
    public Scope getMemberScope() {
        // TODO: add methods
        return new EmptyScope();
    }

    @Override
    public int hashCode() {
        return 12345678;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserNull) {
            return true; // All null objects are the same
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public boolean isTrue() {
        return false;
    }
}
