package daro.lang.interpreter;

import daro.lang.values.UserObject;

/**
 * This interface should be implemented by objects that are used for storing variables at by an assignment statement.
 * e.g. `x = 5` will require `x` to return such an object for storing the value on the right side.
 * 
 * @author Roland Bernard
 */
public interface VariableLocation {

    /**
     * Write the given value into the variable location.
     * 
     * @param value
     *            The value that should be written
     */
    public void storeValue(UserObject value);
}
