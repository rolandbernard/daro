package daro.lang.values;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import daro.lang.interpreter.ExecutionObserver;

/**
 * This {@link UserObject} represents an instance of a function executing a {@link Function}. This is used for build in
 * functions that are not created by the user.
 * 
 * @author Roland Bernard
 */
public class UserLambdaFunction extends UserFunction {
    private final int parameters;
    private final BiFunction<UserObject[], ExecutionObserver[], UserObject> function;

    /**
     * Create a {@link UserFunction} from a parameter count and a {@link Function}.
     * 
     * @param parameters
     *            The number of parameters the function accepts
     * @param function
     *            The {@link Function} the fuction executes
     */
    public UserLambdaFunction(int parameters, Function<UserObject[], UserObject> function) {
        this.parameters = parameters;
        this.function = (params, observers) -> {
            return function.apply(params);
        };
    }

    /**
     * Create a {@link UserFunction} from a parameter count and a {@link Function}.
     * 
     * @param parameters
     *            The number of parameters the function accepts
     * @param function
     *            The {@link BiFunction} the fuction executes
     */
    public UserLambdaFunction(int parameters, BiFunction<UserObject[], ExecutionObserver[], UserObject> function) {
        this.parameters = parameters;
        this.function = function;
    }

    /**
     * Create a {@link UserFunction} from a parameter count and a {@link Consumer}.
     * 
     * @param parameters
     *            The number of parameters the function accepts
     * @param function
     *            The {@link Consumer} the fuction executes
     */
    public UserLambdaFunction(int parameters, Consumer<UserObject[]> function) {
        this.parameters = parameters;
        this.function = (params, observers) -> {
            function.accept(params);
            return null;
        };
    }

    /**
     * Create a {@link UserFunction} from a parameter count and a {@link Consumer}.
     * 
     * @param parameters
     *            The number of parameters the function accepts
     * @param function
     *            The {@link BiConsumer} the fuction executes
     */
    public UserLambdaFunction(int parameters, BiConsumer<UserObject[], ExecutionObserver[]> function) {
        this.parameters = parameters;
        this.function = (params, observers) -> {
            function.accept(params, observers);
            return null;
        };
    }

    /**
     * Create a {@link UserFunction} from a {@link Function} that accepts a variable number of parameters.
     * 
     * @param function
     *            The {@link Function} the fuction executes
     */
    public UserLambdaFunction(Function<UserObject[], UserObject> function) {
        this(-1, function);
    }

    /**
     * Create a {@link UserFunction} from a {@link Consumer} that accepts a variable number of parameters.
     * 
     * @param function
     *            The {@link Consumer} the fuction executes
     */
    public UserLambdaFunction(Consumer<UserObject[]> function) {
        this(-1, function);
    }

    @Override
    public int getParamCount() {
        return parameters;
    }

    @Override
    public UserObject execute(UserObject[] params, ExecutionObserver[] observers) {
        return function.apply(params, observers);
    }

    @Override
    public int hashCode() {
        return (971 * function.hashCode()) ^ (991 * Integer.hashCode(parameters));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserLambdaFunction) {
            UserLambdaFunction func = (UserLambdaFunction) object;
            return function.equals(func.function) && parameters == func.parameters;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (parameters >= 0) {
            return "fn [build-in] (" + String.valueOf(parameters) + ")";
        } else {
            return "fn [build-in] (...)";
        }
    }
}
