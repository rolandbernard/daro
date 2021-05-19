package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.Scope;

/**
 * This class is the superclass for all user objects in the interpreter that represent a type.
 * 
 * @author Roland Bernard
 */
public abstract class UserType extends UserObject {

    @Override
    public UserType getType() {
        return new UserTypeType();
    }

    /**
     * Instantiates a {@link UserObject} of the type represented by this object and initializes it with it's default
     * values.
     * 
     * @return The instantiated object
     */
    public abstract UserObject instantiate();

    /**
     * Instantiates a {@link UserObject} of the type represented by this object and initializes it with the values in
     * the given initializer. The initializer is executed in the given scope, but the resulting object must not be
     * linked to the scope in any other way.
     * 
     * @param scope
     *            The scope to initialize in
     * @param initializer
     *            The initializer to initialize the {@link UserObject} with
     * 
     * @return The instantiated object
     */
    public abstract UserObject instantiate(Scope scope, AstInitializer initializer);

    @Override
    public int hashCode() {
        // This is enough for most of the types (e.g. type, null, integer, real, string, array) and
        // should be overwitten by types that need more (e.g. class).
        return this.getClass().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // This is enough for most of the types (e.g. type, null, integer, real, string) and should be
        // overwitten by types that need more (e.g. class, array).
        return this.getClass() == object.getClass();
    }

    @Override
    public boolean isTrue() {
        return true;
    }
}
