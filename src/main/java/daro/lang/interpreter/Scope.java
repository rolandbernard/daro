package daro.lang.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import daro.lang.values.UserObject;

/**
 * This is the superclass of all scopes. A scope is a collection of variables (with names and values).
 *
 * @author Roland Bernard
 */
public abstract class Scope {
    protected final Scope parent;
    protected final Map<String, UserObject> variables;

    /**
     * Create a new {@link Scope} without a parent.
     */
    public Scope() {
        this(null);
    }

    /**
     * Creates a new {@link Scope} with the given parent.
     * 
     * @param parent
     *            The parent scope
     */
    public Scope(Scope parent) {
        this.parent = parent;
        this.variables = new HashMap<>();
    }

    /**
     * Creates a new {@link Scope} with the given parent and internal map. This constructor is only to be used
     * internally by this class.
     * 
     * @param parent
     *            The parent scope
     * @param variables
     *            The internal variables map
     */
    protected Scope(Scope parent, Map<String, UserObject> variables) {
        this.parent = parent;
        this.variables = variables;
    }

    /**
     * Returns a {@link Scope} that only includes the last level of this scope and the given scope as its parent. This
     * method is used inside classes as the member scope.
     * 
     * @param parent
     *            The parent of
     * 
     * @return The last level scope
     */
    abstract public Scope getFinalLevel(Scope parent);

    /**
     * Returns a {@link Scope} that only includes the last level of this scope. This method is used inside classes as
     * the member scope.
     * 
     * @return The last level scope
     */
    public Scope getFinalLevel() {
        return getFinalLevel(null);
    }

    /**
     * Creates a new variable in the last level of the scope if possible, otherwise throw a
     * {@link InterpreterException}.
     *
     * @param name
     *            The name of the new variable
     * @param object
     *            The object to store into the new variable
     */
    public void newVariableInFinal(String name, UserObject object) {
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
    public boolean containsVariable(String name) {
        if (variables.containsKey(name)) {
            return true;
        } else if (parent != null) {
            return parent.containsVariable(name);
        } else {
            return false;
        }
    }

    /**
     * Returns the value of the variable of the given name in this scope or null if no such variable exists in this
     * scope.
     * 
     * @param name
     *            The name to search for
     * 
     * @return The {@link UserObject} stored in the variable if it exists, null otherwise
     */
    public UserObject getVariableValue(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        } else if (parent != null) {
            return parent.getVariableValue(name);
        } else {
            return null;
        }
    }

    /**
     * Returns the complete mapping of this scope. i.e. A map of every possible name that would return a non-null value
     * from getVariableValue.
     * 
     * @return The complete mapping
     */
    public Map<String, UserObject> getCompleteMapping() {
        Map<String, UserObject> result;
        if (parent != null) {
            result = parent.getCompleteMapping();
        } else {
            result = new HashMap<>();
        }
        result.putAll(variables);
        return result;
    }

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

    @Override
    public int hashCode() {
        return (971 * Objects.hashCode(variables)) ^ (991 * Objects.hashCode(parent));
    }

    @Override
    public boolean equals(Object object) {
        if (getClass() == object.getClass()) {
            Scope scope = (Scope) object;
            return Objects.equals(variables, scope.variables) && Objects.equals(parent, scope.parent);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return getCompleteMapping().entrySet().stream()
                .map(entry -> entry.getKey() + " = " + String.valueOf(entry.getValue()))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
