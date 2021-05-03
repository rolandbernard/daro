package daro.lang.interpreter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a simple scope. A scope is a collection of variables (with names and
 * values). A scope also has a parent which will be inspected if a variable is not found in the
 * current scope.
 * 
 * @author Roland Bernard
 */
public class Scope {
    private final Scope parent;
    private final Map<String, UserObject> variables;

    /**
     * Create a new {@link Scope} without a parent.
     */
    public Scope() {
        this(null);
    }

    /**
     * Creates a new {@link Scope} with the given parent.
     * @param parent
     */
    public Scope(Scope parent) {
        this.parent = parent;
        this.variables = new HashMap<>();
    }

    /**
     * Test whether this scope contans a variable with the given name.
     * @param name The name to search for
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
     * Returns the value of the variable of the given name in this scope or null if no such variable
     * exists in this scope.
     * @param name The name to search for
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
     * Returns a {@link VariableLocation} to store a variable of the given name. If the variable
     * does not exist in this scope, a new one will be created in the lowest scope.
     * @param name The name to search for
     * @return A {@link VariableLocation} to store the variable at
     */
    public VariableLocation getVariableLocation(String name) {
        if (!variables.containsKey(name) && containsVariable(name)) {
            return parent.getVariableLocation(name);
        } else {
            return value -> {
                variables.put(name, value);
            };
        }
    }
}
