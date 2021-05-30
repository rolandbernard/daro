package daro.lang.interpreter;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import daro.lang.values.DaroModule;

/**
 * This class contains all the information required by the executor.
 * 
 * @author Roland Bernard
 */
public class ExecutionContext {
    private final Scope scope;
    private final Map<Path, DaroModule> modules;
    private final PrintStream output;
    private final ExecutionObserver[] observers;

    /**
     * Create a new {@link ExecutionContext} for execution in the given scope and observed by the given
     * {@link ExecutionObserver}s.
     * 
     * @param scope
     *            The scope to execute in
     * @param output
     *            The output for printing functions
     * @param observers
     *            The observers for this execution
     */
    public ExecutionContext(Scope scope, PrintStream output, ExecutionObserver ...observers) {
        this.scope = scope;
        this.observers = observers;
        this.output = output;
        this.modules = new HashMap<>();
    }

    /**
     * Create a new {@link ExecutionContext} for execution in the given scope but copying other data
     * from the given context.
     * 
     * @param context
     *            The context to copy data from
     * @param scope
     *            The scope to execute in
     */
    private ExecutionContext(ExecutionContext context, Scope scope) {
        this.scope = scope;
        this.observers = context.observers;
        this.output = context.output;
        this.modules = context.modules;
    }

    /**
     * Create a new {@link ExecutionContext} for execution with the given observers but copying
     * other data from the given context.
     * 
     * @param context
     *            The context to copy data from
     * @param observers
     *            The observers to execute with
     */
    private ExecutionContext(ExecutionContext context, ExecutionObserver ...observers) {
        this.scope = context.scope;
        this.observers = Arrays.copyOf(context.observers, context.observers.length + observers.length);
        for (int i = 0; i < observers.length; i++) {
            this.observers[context.observers.length + i] = observers[i];
        }
        this.output = context.output;
        this.modules = context.modules;
    }

    /**
     * Return the {@link Scope} associated with this context.
     * 
     * @return The associated scope
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Return the {@link PrintStream} associated with this context.
     * 
     * @return The associated output
     */
    public PrintStream getOutput() {
        return output;
    }

    /**
     * Return the {@link ExecutionObserver} for this context.
     * 
     * @return The observers for this context
     */
    public ExecutionObserver[] getObservers() {
        return observers;
    }

    /**
     * Return the modules loaded for this context.
     * 
     * @return The modules for this context
     */
    public Map<Path, DaroModule> getModules() {
        return modules;
    }

    /**
     * Create a new context that uses the same data as this, but has a different scope.
     *
     * @param scope
     *            The new scope for the resulting context
     * 
     * @return The new {@link ExecutionContext}
     */
    public ExecutionContext forScope(Scope scope) {
        return new ExecutionContext(this, scope);
    }

    /**
     * Create a new context that uses the same data as this, but has additional observers.
     *
     * @param observers
     *            The new observers for the resulting context
     * 
     * @return The new {@link ExecutionContext}
     */
    public ExecutionContext withObservers(ExecutionObserver ...observers) {
        return new ExecutionContext(this, observers);
    }
}
