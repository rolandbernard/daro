package daro.lang.interpreter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import daro.lang.values.DaroObject;

/**
 * This is the superclass of most scopes. A scope is a collection of variables
 * (with names and values).
 *
 * @author Roland Bernard
 */
public abstract class AbstractScope implements Scope {
    protected final Map<String, DaroObject> variables;
    protected Scope[] parents;
    protected int baseParents;

    /**
     * Used to allow for recursive scopes. Be careful if you use it!
     */
    private boolean visited = false;

    /**
     * Creates a new {@link Scope} with the given parent.
     * 
     * @param parent The parent scope
     */
    public AbstractScope(Scope ...parent) {
        this(new HashMap<>(), parent);
    }

    /**
     * Creates a new {@link Scope} with the given parent and internal map. This
     * constructor is only to be used internally by this class.
     * 
     * @param variables The internal variables map
     * @param parent    The parent scope
     */
    protected AbstractScope(Map<String, DaroObject> variables, Scope ...parent) {
        this.parents = parent;
        this.variables = variables;
        this.baseParents = parent.length;
    }

    /**
     * Add the given parents to the scope. These parents will be added to the end
     * and therefore have lower priority that the existing parents.
     *
     * @param parent The parents to add
     */
    public void addParent(Scope ...parent) {
        Scope[] newParents = Arrays.copyOf(parents, parents.length + parent.length);
        for (int i = 0; i < parent.length; i++) {
            newParents[parents.length + i] = parent[i];
        }
        parents = newParents;
    }

    /**
     * This function can be used to safely implement recursion that will be able to
     * handle circular scope graphs.
     *
     * @param <T>      The type the function returns
     * @param function The function to execute if not already inside the scope
     * @param def      The default value to return if we are already in the scope
     * @return The value the function returns
     */
    public <T> T safeRecursion(Supplier<T> function, T def) {
        if (visited) {
            return def;
        } else {
            try {
                visited = true;
                return function.get();
            } finally {
                visited = false;
            }
        }
    }

    @Override
    public abstract Scope getFinalLevel();

    @Override
    public Scope[] getParents() {
        return parents;
    }

    @Override
    public boolean containsVariable(String name) {
        return safeRecursion(() -> {
            if (variables.containsKey(name)) {
                return true;
            } else {
                for (Scope parent : parents) {
                    if (parent.containsVariable(name)) {
                        return true;
                    }
                }
                return false;
            }
        }, false);
    }

    @Override
    public DaroObject getVariableValue(String name) {
        return safeRecursion(() -> {
            if (variables.containsKey(name)) {
                return variables.get(name);
            } else {
                for (Scope parent : parents) {
                    if (parent.containsVariable(name)) {
                        return parent.getVariableValue(name);
                    }
                }
                return null;
            }
        }, null);
    }

    @Override
    public Map<String, DaroObject> getCompleteMapping() {
        Map<String, DaroObject> result = new HashMap<>();
        return safeRecursion(() -> {
            for (int i = parents.length - 1; i >= 0; i--) {
                result.putAll(parents[i].getCompleteMapping());
            }
            result.putAll(variables);
            return result;
        }, result);
    }

    @Override
    public int hashCode() {
        return safeRecursion(() -> {
            return (971 * Objects.hashCode(variables)) ^ (991 * Arrays.hashCode(parents));
        }, 42);
    }

    @Override
    public boolean equals(Object object) {
        return safeRecursion(() -> {
            if (object instanceof AbstractScope) {
                AbstractScope scope = (AbstractScope)object;
                return Objects.equals(variables, scope.variables) && Arrays.equals(parents, scope.parents);
            } else {
                return false;
            }
        }, this == object);
    }

    @Override
    public String toString() {
        return getAsString();
    }
}
