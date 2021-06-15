package daro.lang.interpreter;

import daro.lang.values.DaroObject;

import java.util.Arrays;
import java.util.Map;

/**
 * This class implements a constant scope. It contains the predefined variables
 * that can not be changed again by the user.
 * 
 * @author Roland Bernard
 */
public class ConstantScope extends AbstractScope {

    /**
     * Creates a new constant scope filling it with the given variable mapping, and
     * pointing it to the given parent scope. The parent scope may change
     * independently of the constant scope itself.
     * 
     * @param mapping The parring from names to values to use
     * @param parent  The parent scope
     */
    public ConstantScope(Map<String, DaroObject> mapping, Scope ...parent) {
        super(mapping, parent);
    }

    @Override
    public VariableLocation getVariableLocation(String name) {
        return safeRecursion(() -> {
            // We should not return a location that is not visible (i.e. shadowed by an
            // existing variable)
            if (!variables.containsKey(name)) {
                for (Scope parent : parents) {
                    if (parent.containsVariable(name)) {
                        // If the variable exists here, it will shadow all later parents
                        // (i.e. return even if null)
                        return parent.getVariableLocation(name);
                    }
                }
                // This scope does not contain the variable, try to get a location in any parent
                for (Scope parent : parents) {
                    VariableLocation location = parent.getVariableLocation(name);
                    if (location != null) {
                        return location;
                    }
                }
            }
            return null;
        }, null);
    }

    @Override
    public void reset() {
        safeRecursion(() -> {
            parents = Arrays.copyOf(parents, baseParents);
            for (Scope parent : parents) {
                parent.reset();
            }
            return null;
        }, null);
    }

    @Override
    public Scope getFinalLevel() {
        return new ConstantScope(variables);
    }
}
