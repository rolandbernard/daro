package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.Scope;

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
    public UserObject instantiate(ExecutionObserver[] observers) {
        return this;
    }

    @Override
    public UserObject instantiate(Scope scope, ExecutionObserver[] observers, AstInitializer initializer) {
        if (initializer.getValues().length != 0) {
            throw new InterpreterException(initializer.getPosition(), "Null type can not be initialized");
        } else {
            return instantiate(observers);
        }
    }

    @Override
    public String toString() {
        return "null";
    }
}
