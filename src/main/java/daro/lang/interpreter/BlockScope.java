package daro.lang.interpreter;

import daro.lang.values.DaroObject;

import java.util.Arrays;
import java.util.Map;

/**
 * This class implements a simple scope. A scope is a collection of variables
 * (with names and values). A scope also has a parent which will be inspected if
 * a variable is not found in the current scope.
 * 
 * @author Roland Bernard
 */
public class BlockScope extends AbstractScope {

    /**
     * Creates a new {@link BlockScope} with the given parent.
     * 
     * @param parent The parent scope
     */
    public BlockScope(Scope ...parent) {
        super(parent);
    }

    /**
     * Creates a new {@link BlockScope} with the given parent and internal map. This
     * constructor is only to be used internally by this class.
     * 
     * @param variables The internal variables map
     * @param parent    The parent scope
     */
    private BlockScope(Map<String, DaroObject> variables, Scope ...parent) {
        super(variables, parent);
    }

    @Override
    public Scope getFinalLevel() {
        return new BlockScope(variables);
    }

    @Override
    public VariableLocation getVariableLocation(String name) {
        return safeRecursion(() -> {
            if (!variables.containsKey(name)) {
                for (Scope parent : parents) {
                    if (parent.containsVariable(name)) {
                        // Only if this variable already exists and is writable should we use a
                        // location in a parent scope.
                        VariableLocation location = parent.getVariableLocation(name);
                        if (location != null) {
                            return location;
                        }
                    }
                }
            }
            return value -> {
                variables.put(name, value);
            };
        }, null);
    }

    @Override
    public void reset() {
        safeRecursion(() -> {
            parents = Arrays.copyOf(parents, baseParents);
            for (Scope parent : parents) {
                parent.reset();
            }
            variables.clear();
            return null;
        }, null);
    }
}
