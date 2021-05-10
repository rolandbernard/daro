package daro.lang.interpreter;

import daro.lang.values.UserObject;

/**
 * This class implements an empty scope that does not contain any variables.
 * 
 * @author Roland Bernard
 */
public class EmptyScope implements Scope {

    @Override
    public boolean containsVariable(String name) {
        return false;
    }

    @Override
    public UserObject getVariableValue(String name) {
        return null;
    }

    @Override
    public VariableLocation getVariableLocation(String name) {
        return null;
    }

    @Override
    public int hashCode() {
        return 1234567;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof EmptyScope) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "{}";
    }
}
