package daro.lang.interpreter;

import daro.lang.ast.Position;
import daro.lang.values.UserObject;

/**
 * This represents an exception that is thrown if a return is encountered by the {@link Executor}. This simplifies the
 * implementation by using the Java VMs unrolling mechanism. If this exception is not handled by the executor the
 * return statement was unexpected, i.e. not inside a function call.
 *
 * @author Roland Bernard
 */
public class ReturnException extends InterpreterException {
    private static final long serialVersionUID = 1L;
    private final UserObject value;

    /**
     * Create a {@link ReturnException} for the given position and return value.
     * 
     * @param position
     *            The {@link Position} of the return statement
     * @param value
     *            The value that was returned
     */
    public ReturnException(Position position, UserObject value) {
        super(position, "Unexpected return statement");
        this.value = value;
    }

    /**
     * Get the value that was returned.
     * 
     * @return The returned value
     */
    public UserObject getReturnValue() {
        return value;
    }
}
