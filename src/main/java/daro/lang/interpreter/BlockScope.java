package daro.lang.interpreter;

import daro.lang.values.UserObject;
import java.util.HashMap;
import java.util.Map;

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
     * @param parent
     */
    public BlockScope(Scope parent) {
        this.parent = parent;
        this.variables = new HashMap<>();
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
}
