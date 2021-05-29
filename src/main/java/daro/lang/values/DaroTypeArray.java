package daro.lang.values;

import java.util.ArrayList;

import daro.lang.ast.AstInitializer;
import daro.lang.ast.AstNode;
import daro.lang.interpreter.ExecutionContext;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.InterpreterException;

/**
 * This class represents the type for a array object ({@link DaroArray}).
 * 
 * @author Roland Bernard
 */
public class DaroTypeArray extends DaroType {

    @Override
    public DaroObject instantiate(ExecutionContext context) {
        return new DaroArray(new ArrayList<>());
    }

    @Override
    public DaroObject instantiate(ExecutionContext context, AstInitializer initializer) {
        DaroArray array = (DaroArray) instantiate(context);
        for (AstNode value : initializer.getValues()) {
            DaroObject object = Executor.execute(context, value);
            if (object != null) {
                array.pushValue(object);
            } else {
                throw new InterpreterException(value.getPosition(), "Value must not be undefined");
            }
        }
        return array;
    }

    @Override
    public String toString() {
        return "array";
    }
}
