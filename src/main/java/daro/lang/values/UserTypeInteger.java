package daro.lang.values;

import java.math.BigInteger;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a integer object ({@link UserInteger}).
 * 
 * @author Roland Bernard
 */
public class UserTypeInteger extends UserType {

    @Override
    public UserObject instantiate(ExecutionObserver[] observers) {
        return new UserInteger(BigInteger.ZERO);
    }

    @Override
    public UserObject instantiate(Scope scope, ExecutionObserver[] observers, AstInitializer initializer) {
        if (initializer.getValues().length == 0) {
            return instantiate(observers);
        } else if (initializer.getValues().length != 1) {
            throw new InterpreterException(initializer.getPosition(), "Integers must be initialized with one value");
        } else {
            AstNode value = initializer.getValues()[0];
            UserObject object = Executor.execute(scope, observers, value);
            if (object instanceof UserInteger) {
                return object;
            } else if (object instanceof UserNumber) {
                UserNumber number = (UserNumber) object;
                return new UserInteger(BigInteger.valueOf((long) number.doubleValue()));
            } else if (object != null) {
                throw new InterpreterException(value.getPosition(), "Value can not be converted to an integer");
            } else {
                throw new InterpreterException(value.getPosition(), "Value must not be undefined");
            }
        }
    }

    @Override
    public String toString() {
        return "int";
    }
}
