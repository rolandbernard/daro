package daro.lang.values;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import daro.lang.ast.AstFunction;
import daro.lang.ast.AstSymbol;
import daro.lang.interpreter.BlockScope;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents an instance of a function executing a {@link Function}. This
 * is used for build in functions that are not created by the user.
 * 
 * @author Roland Bernard
 */
public class UserLambdaFunction extends UserFunction {
    private final int parameters;
    private final Function<UserObject[], UserObject> function;

    /**
     * Create a {@link UserFunction} from a parameter count and a {@link Function}.
     * @param parameters The number of parameters the function accepts
     * @param function The {@link Function} the fuction executes
     */
    public UserLambdaFunction(int parameters, Function<UserObject[], UserObject> function) {
        this.parameters = parameters;
        this.function = function;
    }

    /**
     * Create a {@link UserFunction} from a parameter count and a {@link Consumer}.
     * @param parameters The number of parameters the function accepts
     * @param function The {@link Consumer} the fuction executes
     */
    public UserLambdaFunction(int parameters, Consumer<UserObject[]> function) {
        this.parameters = parameters;
        this.function = params -> {
            function.accept(params);
            return null;
        };
    }

    @Override
    public int getParamCount() {
        return parameters;
    }

    @Override
    public UserObject execute(UserObject[] params) {
        return function.apply(params);
    }

    @Override
    public int hashCode() {
        return (971 * function.hashCode()) ^ (991 * Integer.hashCode(parameters));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserLambdaFunction) {
            UserLambdaFunction func = (UserLambdaFunction)object;
            return function.equals(func.function)
                && parameters == func.parameters;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (parameters >= 0) {
            return "function (" + String.valueOf(parameters) + ")";
        } else {
            return "function (...)";
        }
    }
}
