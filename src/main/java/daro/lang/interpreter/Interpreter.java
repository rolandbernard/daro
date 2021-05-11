package daro.lang.interpreter;

import daro.lang.ast.*;
import daro.lang.parser.*;
import daro.lang.values.UserObject;

/**
 * This is the main interface into the daro interpreter.
 * This class implements a simple low setup time interpreter for the Daro language. It is
 * implemented using multiple visitors.
 * 
 * @author Roland Bernard
 */
public class Interpreter {
    private final Scope globalScope;

    /**
     * Create a new {@link Interpreter}. Each interpreter has it's own context in which every
     * execute method will execute it's 
     */
    public Interpreter() {
        globalScope = new BlockScope(new RootScope());
    }

    /**
     * Execute the given {@link AstNode} inside the {@link Interpreter}.
     * @param ast The ast that should be executed
     * @return The result of the execution
     * @throws InterpreterException It the code causes an exception during execution
     */
    public UserObject execute(AstNode ast) {
        return Executor.execute(globalScope, ast);
    }

    /**
     * Parses and executes the code inside the given source. All statements in the source will be
     * executed in the global scope of the interpreter and functions, class and variables in the
     * code will be accessible for subsequent execute calls.
     * @param source The code that should be executed
     * @return The result of the execution
     * @throws InterpreterException It the code causes an exception during execution
     * @throws ParsingException It the source cannot be parsed
     */
    public UserObject execute(String source) {
        // Here we have to execute every statement seperatly because a {link AstBlock} will
        // otherwise create a new scope that will prevent the usage of the global scope.
        // TODO: plug in ScopeInitializer
        AstBlock ast = Parser.parseSourceCode(source);
        UserObject value = null;
        for (AstNode statement : ast.getContent()) {
            value = execute(statement);
        }
        return value;
    }
}

