package daro.lang.interpreter;

import java.io.PrintStream;
import java.nio.file.Path;

import daro.lang.ast.*;
import daro.lang.parser.*;
import daro.lang.values.DaroObject;

/**
 * This is the main interface into the daro interpreter. This class implements a simple low setup time interpreter for
 * the Daro language. It is implemented using multiple visitors ({@link Executor}, {@link LocationEvaluator},
 * {@link ScopeInitializer}) that should not be used manually by the user.
 * 
 * @author Roland Bernard
 */
public class Interpreter {
    private final ExecutionContext context;

    /**
     * Creates a new interpreter for execution in the given {@link Scope}.
     * 
     * @param context
     *            The context to execute in
     */
    public Interpreter(ExecutionContext context) {
        this.context = context;
    }

    /**
     * Create a new {@link Interpreter} using the given {@link PrintStream} as an output for print functions instead of
     * the default System.out.
     * 
     * @param output
     *            The output stream for print functions
     */
    public Interpreter(PrintStream output) {
        this(new ExecutionContext(new BlockScope(new RootScope()), output));
    }

    /**
     * Create a new {@link Interpreter}. Each interpreter has it's own context in which every execute method will
     * execute. This method will initialize the interpreter with a default global scope.
     */
    public Interpreter() {
        this(System.out);
    }

    /**
     * Returns the {@link ExecutionContext} of this {@link Interpreter} instance.
     * 
     * @return The execution context
     */
    public ExecutionContext getContext() {
        return context;
    }

    /**
     * Execute the given {@link AstNode} inside the {@link Interpreter} with the given {@link ExecutionObserver}s.
     * 
     * @param ast
     *            The ast that should be executed
     * @param observers
     *            The observers to execute with
     * 
     * @return The result of the execution
     * 
     * @throws InterpreterException
     *             It the code causes an exception during execution
     */
    public DaroObject execute(AstNode ast, ExecutionObserver ...observers) {
        ScopeInitializer.initialize(context.getScope(), ast);
        return Executor.execute(context.withObservers(observers), ast);
    }

    /**
     * Parses and executes the code inside the given source. All statements in the source will be executed in the global
     * scope of the interpreter and functions, class and variables in the code will be accessible for subsequent execute
     * calls. Also install the given {@link ExecutionObserver}s before execution and uninstall them afterwards.
     * 
     * @param source
     *            The code that should be executed
     * @param observers
     *            The observers to execute with
     * 
     * @return The result of the execution
     * 
     * @throws InterpreterException
     *             It the code causes an exception during execution
     * @throws ParsingException
     *             It the source cannot be parsed
     */
    public DaroObject execute(String source, ExecutionObserver ...observers) {
        AstSequence ast = Parser.parseSourceCode(source);
        return execute(ast, observers);
    }

    /**
     * Parses and executes the code inside the given file. All statements in the source will be executed in the global
     * scope of the interpreter and functions, class and variables in the code will be accessible for subsequent execute
     * calls. Also install the given {@link ExecutionObserver}s before execution and uninstall them afterwards.
     * 
     * @param file
     *            The code that should be executed
     * @param observers
     *            The observers to execute with
     * 
     * @return The result of the execution
     * 
     * @throws InterpreterException
     *             It the code causes an exception during execution
     * @throws ParsingException
     *             It the source cannot be parsed
     */
    public DaroObject execute(Path file, ExecutionObserver ...observers) {
        return Executor.executeFile(context.withObservers(observers), file);
    }

    /**
     * Reset the global scope of this interpreter, clearing all variables.
     */
    public void reset() {
        context.getScope().reset();
    }
}
