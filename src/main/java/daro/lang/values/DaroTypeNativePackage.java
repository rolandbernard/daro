package daro.lang.values;

import daro.lang.ast.AstInitializer;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.InterpreterException;

/**
 * This class represents the type for a package object
 * ({@link DaroNativePackage}).
 * 
 * @author Roland Bernard
 */
public class DaroTypeNativePackage extends DaroType {

    @Override
    public DaroObject instantiate(ExecutionContext context) {
        return new DaroNativePackage("java");
    }

    @Override
    public DaroObject instantiate(ExecutionContext context, AstInitializer initializer) {
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
