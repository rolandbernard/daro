package daro.lang.interpreter;

/**
 * This class contains all the information required by the executor.
 * 
 * @author Roland Bernard
 */
public class ExecutionContext {
    private final Scope scope;
    private final ExecutionObserver[] observers;

    /**
     * Create a new {@link ExecutionContext} for execution in the given scope and observed by the given
     * {@link ExecutionObserver}s.
     * 
     * @param scope
     *            The scope to execute in
     * @param observers
     *            The observers for this execution
     */
    public ExecutionContext(Scope scope, ExecutionObserver[] observers) {
        this.scope = scope;
        this.observers = observers;
    }

    /**
     * Create a new {@link ExecutionContext} for execution in the given scope.
     * 
     * @param scope
     *            The scope to execute in
     */
    public ExecutionContext(Scope scope) {
        this(scope, null);
    }

    /**
     * Create a new empty {@link Executor}.
     */
    public ExecutionContext() {
        this(null, null);
    }

    /**
     * Return the Scope associated with this context.
     * 
     * @return The associated scope
     */
    public Scope getScope() {
        return scope;
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
     * Create a new context that uses the same data as this, but has a different scope.
     *
     * @param scope
     *            The new scope for the resulting context
     * 
     * @return The new {@link ExecutionContext}
     */
    public ExecutionContext forScope(Scope scope) {
        return new ExecutionContext(scope, observers);
    }
}
