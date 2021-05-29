package daro.lang.values;

import daro.lang.interpreter.EmptyScope;
import daro.lang.interpreter.Scope;

/**
 * This class is the superclass for all user objects in the interpreter.
 * 
 * @author Roland Bernard
 */
public abstract class DaroObject {

    /**
     * This function should return the {@link DaroType} of the {@link DaroObject} it is called on.
     * 
     * @return The type of the {@link DaroObject}
     */
    public abstract DaroType getType();

    /**
     * This function should return the {@link Scope} that is used for member access. Objects that have more variables
     * should overwrite this method, but still keep the result from this as a parent scope.
     * 
     * @return The member scope of the object
     */
    public Scope getMemberScope() {
        return new EmptyScope();
    }

    /**
     * This function should return true if the value is considered true for the purpose of conditions in conditional
     * statements. Object that return true for this function do not necessarily have to be equal to the
     * {@link DaroBoolean} of value true.
     * 
     * @return It the object is 'true'
     */
    public abstract boolean isTrue();
}
