package daro.lang.interpreter;

import daro.lang.values.DaroObject;

import java.util.Map;

/**
 * This class implements a constant scope. It contains the predefined variables that can not be changed again by the
 * user.
 * 
 * @author Roland Bernard
 */
public class ConstantScope extends AbstractScope {

    /**
     * Creates a new constant scope filling it with the given variable mapping, and pointing it to the given parent
     * scope. The parent scope may change independently of the constant scope itself.
     * 
     * @param mapping
     *            The parring from names to values to use
     * @param parent
     *            The parent scope
     */
    public ConstantScope(Map<String, DaroObject> mapping, Scope... parent) {
        super(mapping, parent);
    }

    @Override
    public VariableLocation getVariableLocation(String name) {
        if (!visited && !variables.containsKey(name)) {
            try {
                visited = true;
                for (Scope parent : parents) {
                    if (parent.containsVariable(name)) {
                        VariableLocation location = parent.getVariableLocation(name);
                        if (location != null) {
                            return location;
                        }
                    }
                }
                for (Scope parent : parents) {
                    VariableLocation location = parent.getVariableLocation(name);
                    if (location != null) {
                        return location;
                    }
                }
            } finally {
                visited = false;
            }
        }
        return null;
    }

    @Override
    public void reset() {
        // The scope can not change
    }

    @Override
    public Scope getFinalLevel() {
        return new ConstantScope(variables);
    }
}
