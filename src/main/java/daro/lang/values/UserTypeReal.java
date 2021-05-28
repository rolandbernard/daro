package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.InterpreterException;

/**
 * This class represents the type for a real object ({@link UserReal}).
 * 
 * @author Roland Bernard
 */
public class UserTypeReal extends UserType {

    @Override
    public UserObject instantiate(ExecutionContext context) {
        return new UserReal(0);
    }

    @Override
    public UserObject instantiate(ExecutionContext context, AstInitializer initializer) {
        if (initializer.getValues().length == 0) {
            return instantiate(context);
        } else if (initializer.getValues().length != 1) {
            throw new InterpreterException(initializer.getPosition(), "Reals must be initialized with one value");
        } else {
            AstNode value = initializer.getValues()[0];
            UserObject object = Executor.execute(context, value);
            if (object instanceof UserNumber) {
                UserNumber number = (UserNumber) object;
                return new UserReal(number.doubleValue());
            } else if (object != null) {
                throw new InterpreterException(value.getPosition(), "Value can not be converted to a real number");
            } else {
                throw new InterpreterException(value.getPosition(), "Value must not be undefined");
            }
        }
    }

    @Override
    public String toString() {
        return "real";
    }
}
