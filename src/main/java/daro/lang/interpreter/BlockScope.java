package daro.lang.interpreter;

import daro.lang.values.UserObject;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class implements a simple scope. A scope is a collection of variables (with names and
 * values). A scope also has a parent which will be inspected if a variable is not found in the
 * current scope.
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
     * @param parent The parent scope
     */
    public BlockScope(Scope parent) {
        this.parent = parent;
        this.variables = new HashMap<>();
    }

    /**
     * Creates a new {@link BlockScope} with the given parent and internal map. This constructor is only to be
     * used internaly by this class.
     * @param variables The interal variables map
     */
    private BlockScope(Scope parent, Map<String, UserObject> variables) {
        this.parent = parent;
        this.variables = variables;
    }

    /**
     * Returns a {@link BlockScope} that only includes the last level of this scope. This method is
     * used inside classes as the member scope.
     * @return The last level scope
     */
    public BlockScope getFinalLevel() {
        return new BlockScope(null, variables);
    }

    /**
     * Create a new variable that will shadow variables that might already exist in a parent scope.
     * This is to be used when executing functions to set the parameters or for inserting function
     * and class values into the scope.
     * @param name The name of the variable
     * @param value The value it should be set to
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
        if (!variables.containsKey(name) && containsVariable(name)) {
            return parent.getVariableLocation(name);
        } else {
            return value -> {
                variables.put(name, value);
            };
        }
    }

    @Override
    public int hashCode() {
        return (971 * variables.hashCode()) ^ (991 * parent.hashCode());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BlockScope) {
            BlockScope scope = (BlockScope)object;
            return variables.equals(scope.variables)
                && parent.equals(scope.parent);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("{");
        if (parent != null) {
            ret.append(parent.toString());
        }
        ret.append(variables.entrySet().stream()
            .map(entry -> entry.getKey() + " = " + String.valueOf(entry.getValue()))
            .collect(Collectors.joining(", ")));
        ret.append("}");
        return ret.toString();
    }
}
