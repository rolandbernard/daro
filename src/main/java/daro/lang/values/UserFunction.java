package daro.lang.values;

import daro.lang.interpreter.EmptyScope;
import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents an instance of a function.
 * 
 * @author Roland Bernard
 */
public abstract class UserFunction extends UserObject {

    @Override
    public UserType getType() {
        return new UserTypeFunction();
    }

    @Override
    public Scope getMemberScope() {
        // TODO: add methods
        return new EmptyScope();
    }

    /**
     * Get the number of parameters that this function can be called with.
     * @return The number of parameters
     */
    public abstract int getParamCount();

    /**
     * Execute the function with the given parameters. This must be called with the correct amount
     * of parameters otherwise the behavior is undefined.
     * @param params The params to call the function with
     * @return The return value of the function
     */
    public abstract UserObject execute(UserObject[] params);
}
