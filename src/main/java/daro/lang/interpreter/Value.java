package daro.lang.interpreter;

/**
 * This class is the super class for all interpreter values.
 *
 * @author Roland Bernard
 */
public abstract class Value {
    private final Type type;

    /**
     * Creates a value object of the given type. This constructor should be called by subclassed to
     * initialize the object with the correct object type.
     * @param type The type of this value
     */
    public Value(Type type) {
        this.type = type;
    }

    /**
     * Returns the type of the given value.
     * @return The type of the value
     */
    public Type getType() {
        return type;
    }

    /**
     * This method should be implemented by subclasses to execte methods on the {@link Value}.
     * Operations like addition and subtraction should also be implemented as methods that have as
     * their name the operator (i.e. "+", "-", "<<", ">>", etc.). This is done to simplify the
     * implementation interface.
     * @param interpreter The method the interpreter should be run in
     * @param method The name of the method to execute
     * @param parameters The parameters to execute the method with
     * @return The return value of the method
     * @throws A {link UnknownMethodException} if the method is not supported by the value
     */
    public abstract Value executeMethod(Interpreter interpreter, String method, Value[] parameters);
}

