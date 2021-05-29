package daro.lang.values;

import daro.lang.interpreter.ExecutionContext;

/**
 * This {@link DaroObject} represents an instance of a function.
 * 
 * @author Roland Bernard
 */
public abstract class DaroFunction extends DaroObject {

    @Override
    public DaroType getType() {
        return new DaroTypeFunction();
    }

    /**
     * Check if the function accepts the given number of parameters.
     * 
     * @return true if the function accepts the given number of parameters, false otherwise
     */
    public abstract boolean allowsParamCount(int count);

    /**
     * Execute the function with the given parameters. This must be called with the correct amount of parameters
     * otherwise the behavior is undefined.
     * 
     * @param params
     *            The params to call the function with
     * @param context
     *            The context the function is executed in
     * 
     * @return The return value of the function
     */
    public abstract DaroObject execute(DaroObject[] params, ExecutionContext context);

    @Override
    public boolean isTrue() {
        return true;
    }
}
