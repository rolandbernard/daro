package daro.lang.interpreter;

/**
 * This class represents an error thrown by calling a method that is not defined.
 *
 * @author Roland Bernard
 */
public class UnknownMethodException extends InterpreterException {
    private static final long serialVersionUID = 1L;
    private final Value receiver;
    private final String method;
    private final Value[] arguments;

    /**
     * Create a {@link UnknownMethodException} for the given parameters.
     * @param interpreter The interpreter that caused the exception
     * @param receiver The receiver of the method
     * @param method The method that was attempted to be invoked
     * @param arguments The arguments the method was invoked with
     */
    public UnknownMethodException(Interpreter interpreter, Value receiver, String method, Value[] arguments) {
        super(interpreter);
        this.receiver = receiver;
        this.method = method;
        this.arguments = arguments;
    }

    /**
     * Get the receiver that did not supported the method.
     * @return The receiver of the call
     */
    public Value getReceiver() {
        return receiver;
    }

    /**
     * Get the method that is not supported by the receiver.
     * @return The method of the call
     */
    public String getMethod() {
        return method;
    }

    /**
     * Get the arguments that the method was attempted to be called with.
     * @return The arguments of the call
     */
    public Value[] getArguments() {
        return arguments;
    }
}

