package daro.lang.interpreter;

/**
 * This class represents an error thrown during the interpreter execution. It includes a reference
 * to the interpreter in which the exception was thrown.
 *
 * @author Roland Bernard
 */
public class InterpreterException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private final Interpreter interpreter;

    /**
     * Create a {@link InterpreterException} for the given interpreter and message.
     * @param interpreter The interpreter that caused the exception
     * @param message The message for the exception
     */
    public InterpreterException(Interpreter interpreter, String message) {
        super(message);
        this.interpreter = interpreter;
    }

    /**
     * Returns the interpreter the exception applies to.
     * @return The interpreter that caused the exception
     */
    public Interpreter getInterpreter() {
        return interpreter;
    }
}
