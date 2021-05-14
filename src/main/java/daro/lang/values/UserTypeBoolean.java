package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.InterpreterException;
import daro.lang.interpreter.Scope;

/**
 * This class represents the type for a integer object ({@link UserInteger}).
 * 
 * @author Roland Bernard
 */
public class UserTypeBoolean extends UserType {

    @Override
    public UserObject instantiate() {
        return new UserBoolean(false);
    }

    @Override
    public UserObject instantiate(Scope scope, AstInitializer initializer) {
        if (initializer.getValues().length == 0) {
            return instantiate();
        } else if (initializer.getValues().length != 1) {
            throw new InterpreterException(initializer.getPosition(), "Booleans must be initialized with one value");
        } else {
            AstNode value = initializer.getValues()[0];
            UserObject object = Executor.execute(scope, value);
            if (object != null) {
                return new UserBoolean(object.isTrue());
            } else {
                throw new InterpreterException(value.getPosition(), "Value must not be undefined");
            }
        }
    }

    @Override
    public String toString() {
        return "bool";
    }
}
