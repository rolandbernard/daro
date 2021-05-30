package daro.lang.values;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import daro.lang.interpreter.ExecutionContext;

/**
 * This {@link DaroObject} represents an instance of a function executing a {@link Function}. This is used for build in
 * functions that are not created by the user.
 * 
 * @author Roland Bernard
 */
public class DaroLambdaFunction extends DaroFunction {
    private final Predicate<Integer> parameters;
    private final BiFunction<DaroObject[], ExecutionContext, DaroObject> function;

    /**
     * Create a {@link DaroFunction} from a parameter checking function and a {@link Function}.
     * 
     * @param parameters
     *            A {@link Predicate} checking if the function accepts the given number of
     *            parameters
     * @param function
     *            The {@link Function} the function executes
     */
    public DaroLambdaFunction(Predicate<Integer> parameters, BiFunction<DaroObject[], ExecutionContext, DaroObject> function) {
        this.parameters = parameters;
        this.function = function;
    }

    /**
     * Create a {@link DaroFunction} from a parameter count and a {@link Function}.
     * 
     * @param parameters
     *            The number of parameters the function accepts
     * @param function
     *            The {@link Function} the function executes
     */
    public DaroLambdaFunction(int parameters, Function<DaroObject[], DaroObject> function) {
        this(count -> count == parameters, (params, context) -> {
            return function.apply(params);
        });
    }

    /**
     * Create a {@link DaroFunction} from a parameter count and a {@link Consumer}.
     * 
     * @param parameters
     *            The number of parameters the function accepts
     * @param function
     *            The {@link BiConsumer} the function executes
     */
    public DaroLambdaFunction(int parameters, BiConsumer<DaroObject[], ExecutionContext> function) {
        this(count -> count == parameters, (params, context) -> {
            function.accept(params, context);
            return null;
        });
    }

    /**
     * Create a {@link DaroFunction} from a {@link Function} that accepts a variable number of parameters.
     * 
     * @param function
     *            The {@link Consumer} the function executes
     */
    public DaroLambdaFunction(Function<DaroObject[], DaroObject> function) {
        this(count -> true, (params, context) -> {
            return function.apply(params);
        });
    }

    /**
     * Create a {@link DaroFunction} from a {@link Consumer} that accepts a variable number of parameters.
     * 
     * @param function
     *            The {@link Consumer} the function executes
     */
    public DaroLambdaFunction(Consumer<DaroObject[]> function) {
        this(count -> true, (params, context) -> {
            function.accept(params);
            return null;
        });
    }

    /**
     * Create a {@link DaroFunction} from a {@link BiConsumer} that accepts a variable number of parameters.
     * 
     * @param function
     *            The {@link BiConsumer} the function executes
     */
    public DaroLambdaFunction(BiConsumer<DaroObject[], ExecutionContext> function) {
        this(count -> true, (params, context) -> {
            function.accept(params, context);
            return null;
        });
    }

    @Override
    public boolean allowsParamCount(int count) {
        return parameters.test(count);
    }

    @Override
    public DaroObject execute(DaroObject[] params, ExecutionContext context) {
        return function.apply(params, context);
    }

    @Override
    public int hashCode() {
        return (971 * function.hashCode()) ^ (991 * Objects.hashCode(parameters));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DaroLambdaFunction) {
            DaroLambdaFunction func = (DaroLambdaFunction) object;
            return Objects.equals(function, func.function) && Objects.equals(parameters, func.parameters);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "[native function]";
    }
}
