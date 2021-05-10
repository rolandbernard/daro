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
}
