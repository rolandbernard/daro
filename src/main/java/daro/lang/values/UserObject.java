package daro.lang.values;

import daro.lang.interpreter.Scope;

/**
 * This class is the superclass for all user objects in the interpreter.
 * 
 * @author Roland Bernard
 */
public abstract class UserObject {

    /**
     * This function should return the {@link UserType} of the {@link UserObject} it is called on.
     * @return The type of the {@link UserObject}
     */
    public abstract UserType getType();

    /**
     * This function should return the {@link Scope} that is used for member access.
     * @return The member scope of the object
     */
    public abstract Scope getMemberScope();

    /**
     * This function should return true if the value is considered true for the purpose of
     * conditions in conditional statements. Object that return true for this function do not
     * necessarily have to be equal to the {@link UserBoolean} of value true.
     * @return It the object is 'true'
     */
    public abstract boolean isTrue();
}
