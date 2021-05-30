package daro.lang.interpreter;

import daro.lang.values.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the root scope of the program. It contains the
 * predefined methods and types in the Daro language.
 * 
 * @author Roland Bernard
 */
public class RootScope extends ConstantScope {

    /**
     * Creates a new {@link RootScope}.
     */
    public RootScope() {
        super(buildRootVariables(), (new DaroNativePackage()).getMemberScope());
    }

    /**
     * This function generates a mapping between {@link String}s and
     * {@link DaroObject}s that represent all the variables in the root scope of a
     * daro program.
     * 
     * @return The mapping of the root scope
     */
    private static Map<String, DaroObject> buildRootVariables() {
        Map<String, DaroObject> variables = new HashMap<>();
        // Types
        variables.put("int", new DaroTypeInteger());
        variables.put("real", new DaroTypeReal());
        variables.put("bool", new DaroTypeBoolean());
        variables.put("string", new DaroTypeString());
        variables.put("type", new DaroTypeType());
        variables.put("function", new DaroTypeFunction());
        variables.put("array", new DaroTypeArray());
        variables.put("array", new DaroTypeArray());
        variables.put("package", new DaroTypeModule());
        // Values
        variables.put("null", new DaroNull());
        variables.put("true", new DaroBoolean(true));
        variables.put("false", new DaroBoolean(false));
        // Functions
        variables.put("typeof", new DaroLambdaFunction(1, params -> {
            return params[0].getType();
        }));
        variables.put("print", new DaroLambdaFunction((params, context) -> {
            for (DaroObject object : params) {
                context.getOutput().print(object.toString());
            }
        }));
        variables.put("println", new DaroLambdaFunction((params, context) -> {
            for (DaroObject object : params) {
                context.getOutput().print(object.toString());
            }
            context.getOutput().println();
        }));
        return variables;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof RootScope) {
            return true;
        } else {
            return false;
        }
    }
}
