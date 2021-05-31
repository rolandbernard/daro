package daro.lang.values;

import java.util.Arrays;
import java.util.stream.Collectors;

import daro.lang.ast.AstFunction;
import daro.lang.ast.AstSymbol;
import daro.lang.interpreter.BlockScope;
import daro.lang.interpreter.ExecutionObserver;
import daro.lang.interpreter.Executor;
import daro.lang.interpreter.ReturnException;
import daro.lang.interpreter.Scope;

/**
 * This {@link UserObject} represents an instance of a function executing from a ast.
 * 
 * @author Roland Bernard
 */
public class UserAstFunction extends UserFunction {
    private final Scope scope;
    private final AstFunction ast;

    /**
     * Create a new function from a scope and ast. The function will normally be executed either in the global scope or
     * in a class scope.
     * 
     * @param scope
     *            The scope to execute the function in
     * @param ast
     *            The ast that represents the function
     */
    public UserAstFunction(Scope scope, AstFunction ast) {
        this.scope = scope;
        this.ast = ast;
    }

    @Override
    public int getParamCount() {
        return ast.getParameters().length;
    }

    @Override
    public UserObject execute(UserObject[] params, ExecutionObserver[] observers) {
        BlockScope parameterScope = new BlockScope(scope);
        AstSymbol[] parameters = ast.getParameters();
        for (int i = 0; i < params.length && i < parameters.length; i++) {
            parameterScope.forceNewVariable(parameters[i].getName(), params[i]);
        }
        try {
            return Executor.execute(parameterScope, observers, ast.getBody());
        } catch (ReturnException returned) {
            return returned.getReturnValue();
        }
    }

    @Override
    public int hashCode() {
        return (971 * scope.hashCode()) ^ (991 * ast.hashCode());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserAstFunction) {
            UserAstFunction function = (UserAstFunction) object;
            return scope.equals(function.scope) && ast.equals(function.ast);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "function " + (ast.getName() != null ? ast.getName() : "") + Arrays.stream(ast.getParameters())
                .map(AstSymbol::getName).collect(Collectors.joining(", ", "(", ")"));
    }
}
