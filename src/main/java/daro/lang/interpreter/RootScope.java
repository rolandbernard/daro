package daro.lang.interpreter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the root scope of the program. It contains the predefined methods and
 * types in the Daro language.
 * 
 * @author Roland Bernard
 */
public class RootScope implements Scope {
    private static final Map<String, UserObject> variables;

    static {
        variables = new HashMap<>();
        // TODO: fill the root scope
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
        return value -> {
            throw new InterpreterException(null, "Global values can not be written to");
        };
    }
}
