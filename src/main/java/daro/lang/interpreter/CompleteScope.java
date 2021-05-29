package daro.lang.interpreter;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import daro.lang.values.*;

/**
 * This class implements a scope that contains all possible keys. If the value of a key is
 * requested, it if found using an internal function. This scope is also not writable.
 * 
 * @author Roland Bernard
 */
public class CompleteScope implements Scope {
    private final Function<String, DaroObject> mapping;

    /**
     * Creates a new complete scope using the given mapping;
     * 
     * @param mapping
     *            The function to use for mapping variable names onto values
     */
    public CompleteScope(Function<String, DaroObject> mapping) {
        this.mapping = mapping;
    }

    @Override
    public Scope getFinalLevel() {
        return this;
    }

    @Override
    public boolean containsVariable(String name) {
        return true;
    }

    @Override
    public DaroObject getVariableValue(String name) {
        return mapping.apply(name);
    }

    @Override
    public Map<String, DaroObject> getCompleteMapping() {
        throw new InterpreterException("Scope can not be iterated over");
    }

    @Override
    public VariableLocation getVariableLocation(String name) {
        return null;
    }

    @Override
    public void reset() {
        // This scope can not be changed
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mapping);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CompleteScope) {
            CompleteScope scope = (CompleteScope) object;
            return Objects.equals(mapping, scope.mapping);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "{...}";
    }
}
