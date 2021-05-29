package daro.lang.interpreter;

import java.util.Map;

import daro.lang.values.DaroObject;

/**
 * This interface should be implement by all scopes. A scope is a collection of variables (with names and values).
 *
 * @author Roland Bernard
 */
public interface Scope {

    /**
     * Returns a {@link Scope} that only includes the last level of this scope. This method is used inside classes as
     * the member scope.
     * 
     * @return The last level scope
     */
    public Scope getFinalLevel();

    /**
     * Creates a new variable in the last level of the scope if possible, otherwise throw a
     * {@link InterpreterException}.
     *
     * @param name
     *            The name of the new variable
     * @param object
     *            The object to store into the new variable
     */
    default public void newVariableInFinal(String name, DaroObject object) {
        VariableLocation location = getFinalLevel().getVariableLocation(name);
        if (location == null) {
            throw new InterpreterException("The surrounding scope does not support function or class definitions");
        } else {
            location.storeValue(object);
        }
    }

    /**
     * Test whether this scope contains a variable with the given name.
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
     * @return The {@link DaroObject} stored in the variable if it exists, null otherwise
     */
    public DaroObject getVariableValue(String name);

    /**
     * Returns the complete mapping of this scope. i.e. A map of every possible name that would return a non-null value
     * from getVariableValue.
     * 
     * @return The complete mapping
     */
    public Map<String, DaroObject> getCompleteMapping();

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
    abstract public VariableLocation getVariableLocation(String name);

    /**
     * Resets the scope to its default state.
     */
    abstract public void reset();
}
