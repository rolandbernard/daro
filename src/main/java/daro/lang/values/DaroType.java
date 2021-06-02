package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.ExecutionContext;

/**
 * This class is the superclass for all user objects in the interpreter that
 * represent a type.
 * 
 * @author Roland Bernard
 */
public abstract class DaroType extends DaroObject {

    @Override
    public DaroType getType() {
        return new DaroTypeType();
    }

    /**
     * Instantiates a {@link DaroObject} of the type represented by this object and
     * initializes it with it's default values.
     * 
     * @param context The context for this initialization
     * @return The instantiated object
     */
    public abstract DaroObject instantiate(ExecutionContext context);

    /**
     * Instantiates a {@link DaroObject} of the type represented by this object and
     * initializes it with it's default values.
     * 
     * @return The instantiated object
     */
    public DaroObject instantiate() {
        return instantiate(null);
    }

    /**
     * Instantiates a {@link DaroObject} of the type represented by this object and
     * initializes it with the values in the given initializer. The initializer is
     * executed in the given context, but the resulting object must not be linked to
     * the scope in any other way.
     * 
     * @param context     The context to initialize in
     * @param initializer The initializer to initialize the {@link DaroObject} with
     * @return The instantiated object
     */
    public abstract DaroObject instantiate(ExecutionContext context, AstInitializer initializer);

    @Override
    public int hashCode() {
        // This is enough for most of the types (e.g. type, null, integer, real, string,
        // array) and
        // should be overwritten by types that need more (e.g. class).
        return this.getClass().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // This is enough for most of the types (e.g. type, null, integer, real, string)
        // and should be
        // overwritten by types that need more (e.g. class, array).
        return this.getClass() == object.getClass();
    }

    @Override
    public boolean isTrue() {
        return true;
    }
}
