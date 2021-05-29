package daro.lang.interpreter;

import daro.lang.values.UserObject;

import java.util.Map;

/**
 * This class implements a constant scope. It contains the predefined variables that can not be changed again by the
 * user.
 * 
 * @author Roland Bernard
 */
public class ConstantScope extends Scope {

    /**
     * Creates a new constant scope filling it with the given variable mapping.
     * 
     * @param mapping
     *            The parring from names to values to use
     */
    public ConstantScope(Map<String, UserObject> mapping) {
        this(null, mapping);
    }

    /**
     * Creates a new constant scope filling it with the given variable mapping, and pointing it to the given parent
     * scope. The parent scope may change independently of the constant scope itself.
     * 
     * @param parent
     *            The parent scope
     * @param mapping
     *            The parring from names to values to use
     */
    public ConstantScope(Scope parent, Map<String, UserObject> mapping) {
        super(parent, mapping);
    }

    @Override
    public VariableLocation getVariableLocation(String name) {
        if (variables.containsKey(name) || parent == null) {
            return null;
        } else {
            return parent.getVariableLocation(name);
        }
    }

    @Override
    public void reset() {
        // The scope can not change
    }

    @Override
    public Scope getFinalLevel(Scope parent) {
        return new ConstantScope(parent, variables);
    }
}
