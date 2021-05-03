package daro.lang.interpreter;

/**
 * This interface should be implemented by objects that are used for storing variables at by an
 * assignment statement. e.g. `x = 5` will require `x` to return such an object for storing the
 * value on the right side.
 * 
 * @author Roland Bernard
 */
public interface VariableLocation {

    public void storeValue(UserObject value);
}
