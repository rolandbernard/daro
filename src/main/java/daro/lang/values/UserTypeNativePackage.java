package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.InterpreterException;

/**
 * This class represents the type for a package object ({@link UserNativePackage}).
 * 
 * @author Roland Bernard
 */
public class UserTypeNativePackage extends UserType {

    @Override
    public UserObject instantiate(ExecutionContext context) {
        return new UserNativePackage("java");
    }

    @Override
    public UserObject instantiate(ExecutionContext context, AstInitializer initializer) {
        if (initializer.getValues().length != 0) {
            throw new InterpreterException(initializer.getPosition(), "Native package type can not be initialized");
        } else {
            return instantiate(context);
        }
    }

    @Override
    public String toString() {
        return "[native package]";
    }
}
