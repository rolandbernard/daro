package daro.lang.interpreter;

import daro.lang.values.UserObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class implements a simple scope. A scope is a collection of variables (with names and values). A scope also has
 * a parent which will be inspected if a variable is not found in the current scope.
 * 
 * @author Roland Bernard
 */
public class BlockScope implements Scope {
    private final Scope parent;
    private final Map<String, UserObject> variables;

    /**
     * Create a new {@link BlockScope} without a parent.
     */
    public BlockScope() {
        this(null);
    }

    /**
     * Creates a new {@link BlockScope} with the given parent.
     * 
     * @param parent
     *            The parent scope
     */
    public BlockScope(Scope parent) {
        this.parent = parent;
        this.variables = new HashMap<>();
    }

    /**
     * Creates a new {@link BlockScope} with the given parent and internal map. This constructor is only to be used
     * internally by this class.
     * 
     * @param parent
     *            The parent scope
     * @param variables
     *            The internal variables map
     */
    private BlockScope(Scope parent, Map<String, UserObject> variables) {
        this.parent = parent;
        this.variables = variables;
    }

    /**
     * Returns a {@link BlockScope} that only includes the last level of this scope and the given scope as its parent.
     * This method is used inside classes as the member scope.
     * 
     * @param parent
     *            The parent of
     * 
     * @return The last level scope
     */
    public BlockScope getFinalLevel(Scope parent) {
        return new BlockScope(parent, variables);
    }

    /**
     * Returns a {@link BlockScope} that only includes the last level of this scope. This method is used inside classes
     * for printing to string.
     * 
     * @return The last level scope
     */
    public BlockScope getFinalLevel() {
        return getFinalLevel(null);
    }

    /**
     * Create a new variable that will shadow variables that might already exist in a parent scope. This is to be used
     * when executing functions to set the parameters or for inserting function and class values into the scope.
     * 
     * @param name
     *            The name of the variable
     * @param value
     *            The value it should be set to
     */
    public void forceNewVariable(String name, UserObject value) {
        variables.put(name, value);
    }

    @Override
    public boolean containsVariable(String name) {
        if (variables.containsKey(name)) {
            return true;
        } else if (parent != null) {
            return parent.containsVariable(name);
        } else {
            return false;
        }
    }

    @Override
    public UserObject getVariableValue(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        } else if (parent != null) {
            return parent.getVariableValue(name);
        } else {
            return null;
        }
    }

    @Override
    public VariableLocation getVariableLocation(String name) {
        if (!variables.containsKey(name) && parent != null && parent.containsVariable(name)) {
            return parent.getVariableLocation(name);
        } else {
            return value -> {
                variables.put(name, value);
            };
        }
    }

    @Override
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

    @Override
    public void reset() {
        parent.reset();
        variables.clear();
    }

    @Override
    public int hashCode() {
        return (971 * variables.hashCode()) ^ (991 * parent.hashCode());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BlockScope) {
            BlockScope scope = (BlockScope) object;
            return variables.equals(scope.variables) && Objects.equals(parent, scope.parent);
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
