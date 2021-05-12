package daro.lang.interpreter;

import daro.lang.values.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class implements the root scope of the program. It contains the predefined methods and
 * types in the Daro language.
 * 
 * @author Roland Bernard
 */
public class RootScope implements Scope {
    private static final Map<String, UserObject> variables;

    static {
        variables = new HashMap<>();
        // Types
        variables.put("nulltype", new UserTypeNull());
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
        // Functions
        variables.put("typeof", new UserLambdaFunction(1, params -> {
            return params[0].getType();
        }));
        variables.put("print", new UserLambdaFunction(-1, params -> {
            for (UserObject object : params) {
                System.out.print(object.toString());
            }
        }));
        variables.put("println", new UserLambdaFunction(-1, params -> {
            for (UserObject object : params) {
                System.out.print(object.toString());
            }
            System.out.println();
        }));
        // TODO: fill the root scope
    }

    @Override
    public boolean containsVariable(String name) {
        return variables.containsKey(name);
    }

    @Override
    public UserObject getVariableValue(String name) {
        return variables.get(name);
    }

    @Override
    public VariableLocation getVariableLocation(String name) {
        return null;
    }

    @Override
    public int hashCode() {
        return variables.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof RootScope) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("{");
        ret.append(variables.entrySet().stream()
            .map(entry -> entry.getKey() + " = " + String.valueOf(entry.getValue()))
            .collect(Collectors.joining(", ")));
        ret.append("}");
        return ret.toString();
    }
}
