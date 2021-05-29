package daro.lang.values;

import java.util.Objects;

import daro.lang.interpreter.Scope;

/**
 * This {@link DaroObject} represents a package. A package is a basically only a collection of
 * variables that can be accessed using the member scope.
 * 
 * @author Roland Bernard
 */
public class DaroModule extends DaroObject {
    private final Scope scope;

    /**
     * Create a new package object from the given scope.
     * 
     * @param scope
     *            The scope the {@link DaroModule} represents
     */
    public DaroModule(Scope scope) {
        this.scope = scope;
    }

    @Override
    public Scope getMemberScope() {
        return scope;
    }

    @Override
    public DaroType getType() {
        return new DaroTypeModule();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(scope);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroModule) {
            DaroModule pack = (DaroModule) object;
            return Objects.equals(scope, pack.getMemberScope());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(scope);
    }

    @Override
    public boolean isTrue() {
        return true;
    }
}
