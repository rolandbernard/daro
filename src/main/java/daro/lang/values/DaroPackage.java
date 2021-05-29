package daro.lang.values;

import java.util.Objects;

import daro.lang.interpreter.Scope;

/**
 * This {@link DaroObject} represents a package. A package is a basically only a collection of
 * variables that can be accessed using the member scope.
 * 
 * @author Roland Bernard
 */
public class DaroPackage extends DaroObject {
    private final Scope scope;

    /**
     * Create a new package object from the given scope.
     * 
     * @param scope
     *            The scope the {@link DaroPackage} represents
     */
    public DaroPackage(Scope scope) {
        this.scope = scope;
    }

    @Override
    public Scope getMemberScope() {
        return scope;
    }

    @Override
    public DaroType getType() {
        return new DaroTypePackage();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(scope);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroPackage) {
            DaroPackage pack = (DaroPackage) object;
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
