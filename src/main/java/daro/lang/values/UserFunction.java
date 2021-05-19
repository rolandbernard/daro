package daro.lang.values;

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

    /**
     * Get the number of parameters that this function can be called with.
     * 
     * @return The number of parameters, or a negative integer if there is no limit (i.e. varargs)
     */
    public abstract int getParamCount();

    /**
     * Execute the function with the given parameters. This must be called with the correct amount of parameters
     * otherwise the behavior is undefined.
     * 
     * @param params
     *            The params to call the function with
     * 
     * @return The return value of the function
     */
    public abstract UserObject execute(UserObject[] params);

    @Override
    public boolean isTrue() {
        return true;
    }
}
