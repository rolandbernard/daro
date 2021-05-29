package daro.lang.interpreter;

import daro.lang.values.*;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the root scope of the program. It contains the predefined methods and types in the Daro
 * language.
 * 
 * @author Roland Bernard
 */
public class RootScope extends ConstantScope {

    /**
     * Creates a new {@link RootScope}. The created scope will use the given System.out as a target for the print
     * functions.
     */
    public RootScope() {
        this(System.out);
    }

    /**
     * Creates a new {@link RootScope}. The created scope will use the given output stream as a target for the print
     * functions.
     * 
     * @param output
     *            The output stream for print functions
     */
    public RootScope(PrintStream output) {
        super(buildRootVariables(output));
    }

    /**
     * This function generates a mapping between {@link String}s and {@link UserObject}s that represent all the
     * variables in the root scope of a daro program.
     * 
     * @param output
     *            The stream to use for print functions
     * 
     * @return The mapping of the root scope
     */
    private static Map<String, UserObject> buildRootVariables(PrintStream output) {
        Map<String, UserObject> variables = new HashMap<>();
        // Types
        variables.put("int", new UserTypeInteger());
        variables.put("real", new UserTypeReal());
        variables.put("bool", new UserTypeBoolean());
        variables.put("string", new UserTypeString());
        variables.put("type", new UserTypeType());
        variables.put("function", new UserTypeFunction());
        variables.put("array", new UserTypeArray());
        variables.put("array", new UserTypeArray());
        // Values
        variables.put("null", new UserNull());
        variables.put("true", new UserBoolean(true));
        variables.put("false", new UserBoolean(false));
        variables.put("java", new UserNativePackage("java"));
        // Functions
        variables.put("typeof", new UserLambdaFunction(1, params -> {
            return params[0].getType();
        }));
        variables.put("print", new UserLambdaFunction(params -> {
            for (UserObject object : params) {
                output.print(object.toString());
            }
        }));
        variables.put("println", new UserLambdaFunction(params -> {
            for (UserObject object : params) {
                output.print(object.toString());
            }
            output.println();
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
