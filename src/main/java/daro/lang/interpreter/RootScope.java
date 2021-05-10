package daro.lang.interpreter;

import daro.lang.values.UserObject;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        return null;
    }

    @Override
    public int hashCode() {
        return variables.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof RootScope) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("{");
        ret.append(variables.entrySet().stream()
            .map(entry -> entry.getKey() + " = " + String.valueOf(entry.getValue()))
            .collect(Collectors.joining(", ")));
        ret.append("}");
        return ret.toString();
    }
}
