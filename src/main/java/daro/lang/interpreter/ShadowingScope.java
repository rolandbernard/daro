package daro.lang.interpreter;

import daro.lang.values.DaroObject;

import java.util.Map;

/**
 * This class implements a scope that will when written to, will always shadow
 * the variables of the parent instead of delegating storing to a parent
 * containing the variable. This scope can be used to protect the parent scope
 * from any kind of manipulation caused by execution inside this scope.
 * 
 * @author Roland Bernard
 */
public class ShadowingScope extends AbstractScope {

    /**
     * Creates a new {@link ShadowingScope} with the given parent.
     * 
     * @param parent The parent scope
     */
    public ShadowingScope(Scope ...parent) {
        super(parent);
    }

    /**
     * Creates a new {@link BlockScope} with the given parent and internal map. This
     * constructor is only to be used internally by this class.
     * 
     * @param variables The internal variables map
     * @param parent    The parent scope
     */
    private ShadowingScope(Map<String, DaroObject> variables, Scope ...parent) {
        super(variables, parent);
    }

    @Override
    public Scope getFinalLevel() {
        return new ShadowingScope(variables);
    }

    @Override
    public VariableLocation getVariableLocation(String name) {
        return value -> {
            variables.put(name, value);
        };
    }

    @Override
    public void reset() {
        // Only clear local variables
        variables.clear();
    }
}
