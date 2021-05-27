package daro.lang.interpreter;

import daro.lang.values.UserObject;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class implements a constant scope. It contains the predefined variables that can not be changed again by the
 * user.
 * 
 * @author Roland Bernard
 */
public class ConstantScope implements Scope {
    protected final Map<String, UserObject> variables;
    private final Scope parent;

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
        this.parent = parent;
        variables = Map.copyOf(mapping);
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
        return null;
    }

    @Override
    public int hashCode() {
        return variables.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ConstantScope) {
            ConstantScope scope = (ConstantScope) object;
            return variables.equals(scope.variables) && Objects.equals(parent, scope.parent);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("{");
        ret.append(variables.entrySet().stream().map(entry -> entry.getKey() + " = " + String.valueOf(entry.getValue()))
                .collect(Collectors.joining(", ")));
        ret.append("}");
        return ret.toString();
    }

    @Override
    public Map<String, UserObject> getCompleteMapping() {
        Map<String, UserObject> result;
        if (parent != null) {
            result = parent.getCompleteMapping();
        } else {
            result = Map.copyOf(Map.of());
        }
        for (Map.Entry<String, UserObject> entry : variables.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
