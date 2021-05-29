package daro.lang.interpreter;

import daro.lang.values.UserObject;
import java.util.Map;

/**
 * This class implements a simple scope. A scope is a collection of variables (with names and values). A scope also has
 * a parent which will be inspected if a variable is not found in the current scope.
 * 
 * @author Roland Bernard
 */
public class BlockScope extends AbstractScope {

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
        super(parent);
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
        super(parent, variables);
    }

    @Override
    public Scope getFinalLevel(Scope parent) {
        return new BlockScope(parent, variables);
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
    public void reset() {
        parent.reset();
        variables.clear();
    }

}
