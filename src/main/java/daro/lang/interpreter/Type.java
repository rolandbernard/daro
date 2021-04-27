package daro.lang.interpreter;

/**
 * This class is the super class for all interpreter types.
 *
 * @author Roland Bernard
 */
public abstract class Type {

    /**
     * This method instantiates a object of the corresponding interpreter {@link Value}.
     * @return The created {@link Value} object
     */
    public abstract Value instantiate();
}

