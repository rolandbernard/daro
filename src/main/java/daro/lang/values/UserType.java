package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.EmptyScope;
import daro.lang.interpreter.Scope;

/**
 * This class is the superclass for all user objects in the interpreter that
 * represent a type.
 * 
 * @author Roland Bernard
 */
public abstract class UserType extends UserObject {

    @Override
    public UserType getType() {
        return new UserTypeType();
    }

    @Override
    public Scope getMemberScope() {
        // TODO: add methods
        return new EmptyScope();
    }

    /**
     * Instantiates a {@link UserObject} of the type represented by this object and initializes it
     * with default values.
     * @return The instantiated object
     */
    public abstract UserObject instantiate();

    /**
     * Instantiates a {@link UserObject} of the type represented by this object and initializes it
     * with the values in the given initializer.
     * @param initializer The initializer to initialize the {@link UserObject} with
     * @return The instantiated object
     */
    public abstract UserObject instantiate(AstInitializer initializer);

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
}
