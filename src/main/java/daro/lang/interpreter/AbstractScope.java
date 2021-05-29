package daro.lang.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import daro.lang.values.DaroObject;

/**
 * This is the superclass of most scopes. A scope is a collection of variables (with names and values).
 *
 * @author Roland Bernard
 */
public abstract class AbstractScope implements Scope {
    protected final Scope parent;
    protected final Map<String, DaroObject> variables;

    /**
     * Create a new {@link Scope} without a parent.
     */
    public AbstractScope() {
        this(null);
    }

    /**
     * Creates a new {@link Scope} with the given parent.
     * 
     * @param parent
     *            The parent scope
     */
    public AbstractScope(Scope parent) {
        this.parent = parent;
        this.variables = new HashMap<>();
    }

    /**
     * Creates a new {@link Scope} with the given parent and internal map. This constructor is only to be used
     * internally by this class.
     * 
     * @param parent
     *            The parent scope
     * @param variables
     *            The internal variables map
     */
    protected AbstractScope(Scope parent, Map<String, DaroObject> variables) {
        this.parent = parent;
        this.variables = variables;
    }

    @Override
    abstract public Scope getFinalLevel(Scope parent);

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
    public DaroObject getVariableValue(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        } else if (parent != null) {
            return parent.getVariableValue(name);
        } else {
            return null;
        }
    }

    @Override
    public Map<String, DaroObject> getCompleteMapping() {
        Map<String, DaroObject> result;
        if (parent != null) {
            result = parent.getCompleteMapping();
        } else {
            result = new HashMap<>();
        }
        result.putAll(variables);
        return result;
    }

    @Override
    public int hashCode() {
        return (971 * Objects.hashCode(variables)) ^ (991 * Objects.hashCode(parent));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AbstractScope) {
            AbstractScope scope = (AbstractScope) object;
            return Objects.equals(variables, scope.variables) && Objects.equals(parent, scope.parent);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return getCompleteMapping().entrySet().stream()
                .map(entry -> entry.getKey() + " = " + String.valueOf(entry.getValue()))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
