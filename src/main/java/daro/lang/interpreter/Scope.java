package daro.lang.interpreter;

import java.util.Map;

import daro.lang.values.UserObject;

/**
 * This interface should be implemented by all of the scope classes. A scope is a collection of variables (with names
 * and values).
 *
 * @author Roland Bernard
 */
public interface Scope {

    /**
     * Test whether this scope contans a variable with the given name.
     * 
     * @param name
     *            The name to search for
     * 
     * @return true if the scope contains such a variable, false otherwise
     */
    public boolean containsVariable(String name);

    /**
     * Returns the value of the variable of the given name in this scope or null if no such variable exists in this
     * scope.
     * 
     * @param name
     *            The name to search for
     * 
     * @return The {@link UserObject} stored in the variable if it exists, null otherwise
     */
    public UserObject getVariableValue(String name);

    /**
     * Returns a {@link VariableLocation} to store a variable of the given name. If the variable does not exist in this
     * scope, a new one will be created in the lowest scope. If the variable does not support writing to, this method
     * may return `null` (e.g. you can not overwrite false).
     * 
     * @param name
     *            The name to search for
     * 
     * @return A {@link VariableLocation} to store the variable at
     */
    public VariableLocation getVariableLocation(String name);

    /**
     * Returns the complete mapping of this scope. i.e. A map of every possible name that would return a non-null value
     * from getVariableValue.
     * 
     * @return The complete mapping
     */
    public Map<String, UserObject> getCompleteMapping();

    /**
     * Resets the scope to its default state.
     */
    public void reset();
}
