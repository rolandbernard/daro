package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.InterpreterException;

/**
 * This class represents both the null object and null type. This means that {@code typeof (null) == null}.
 * 
 * @author Roland Bernard
 */
public class UserNull extends UserType {

    @Override
    public UserType getType() {
        return new UserNull();
    }

    @Override
    public UserObject instantiate(ExecutionContext context) {
        return this;
    }

    @Override
    public UserObject instantiate(ExecutionContext context, AstInitializer initializer) {
        if (initializer.getValues().length != 0) {
            throw new InterpreterException(initializer.getPosition(), "Null type can not be initialized");
        } else {
            return instantiate(context);
        }
    }

    @Override
    public String toString() {
        return "null";
    }
}
