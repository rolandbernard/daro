package daro.lang.interpreter;

import java.util.Arrays;
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
    protected final Map<String, DaroObject> variables;
    protected Scope[] parents;

    /**
     * Used to allow for recursive scopes. Be careful if you use it!
     */
    protected boolean visited = false;

    /**
     * Creates a new {@link Scope} with the given parent.
     * 
     * @param parent
     *            The parent scope
     */
    public AbstractScope(Scope... parent) {
        this.parents = parent;
        this.variables = new HashMap<>();
    }

    /**
     * Creates a new {@link Scope} with the given parent and internal map. This constructor is only to be used
     * internally by this class.
     * 
     * @param variables
     *            The internal variables map
     * @param parent
     *            The parent scope
     */
    protected AbstractScope(Map<String, DaroObject> variables, Scope[] parent) {
        this.parents = parent;
        this.variables = variables;
    }

    public void addParent(Scope... parent) {
        Scope[] newParents = Arrays.copyOf(parents, parents.length + parent.length);
        for (int i = 0; i < parent.length; i++) {
            newParents[parents.length + i] = parent[i];
        }
        parents = newParents;
    }

    @Override
    abstract public Scope getFinalLevel();

    @Override
    public boolean containsVariable(String name) {
        if (visited) {
            return false;
        } else if (variables.containsKey(name)) {
            return true;
        } else {
            try {
                visited = true;
                for (Scope parent : parents) {
                    if (parent.containsVariable(name)) {
                        return true;
                    }
                }
                return false;
            } finally {
                visited = false;
            }
        }
    }

    @Override
    public DaroObject getVariableValue(String name) {
        if (visited) {
            return null;
        } else if (variables.containsKey(name)) {
            return variables.get(name);
        } else {
            try {
                visited = true;
                for (Scope parent : parents) {
                    if (parent.containsVariable(name)) {
                        return parent.getVariableValue(name);
                    }
                }
                return null;
            } finally {
                visited = false;
            }
        }
    }

    @Override
    public Map<String, DaroObject> getCompleteMapping() {
        Map<String, DaroObject> result = new HashMap<>();
        if (!visited) {
            try {
                visited = true;
                for (int i = parents.length - 1; i >= 0; i--) {
                    result.putAll(parents[i].getCompleteMapping());
                }
                result.putAll(variables);
            } finally {
                visited = false;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return (971 * Objects.hashCode(variables)) ^ (991 * Arrays.hashCode(parents));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AbstractScope) {
            AbstractScope scope = (AbstractScope) object;
            return Objects.equals(variables, scope.variables) && Arrays.equals(parents, scope.parents);
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
