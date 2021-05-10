package daro.lang.interpreter;

import daro.lang.values.UserObject;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the root scope of the program. It contains the predefined methods and
 * types in the Daro language.
 * 
 * @author Roland Bernard
 */
public class ConstantScope implements Scope {
    private final Map<String, UserObject> variables;

    public ConstantScope() {
        variables = Map.of();
    }

    public ConstantScope(Map<String, UserObject> mapping) {
        variables = Map.copyOf(mapping);
    }

    @Override
    public boolean containsVariable(String name) {
        return variables.containsKey(name);
    }

    @Override
    public UserObject getVariableValue(String name) {
        return variables.get(name);
    }

    @Override
    public VariableLocation getVariableLocation(String name) {
        return null;
    }
}
