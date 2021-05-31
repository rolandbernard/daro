package daro.lang.interpreter;

import daro.lang.ast.*;
import daro.lang.values.*;

/**
 * This class is used to execute an ast inside a given scope and return the variable location that corresponts to the
 * ast.
 *
 * @author Roland Bernard
 */
public class LocationEvaluator implements Visitor<VariableLocation> {
    private final Scope scope;
    private final ExecutionObserver[] observers;

    /**
     * Create a new {@link LocationEvaluator} for execution in the given scope and observed by the given
     * {@link ExecutionObserver}s.
     * 
     * @param scope
     *            The scope to execute in
     * @param observers
     *            The observers for this execution
     */
    public LocationEvaluator(Scope scope, ExecutionObserver[] observers) {
        this.scope = scope;
        this.observers = observers;
    }

    /**
     * Create a new {@link LocationEvaluator} for execution in the given scope.
     * 
     * @param scope
     *            The scope to execute in
     */
    public LocationEvaluator(Scope scope) {
        this(scope, null);
    }

    /**
     * Run the given {@link AstNode} in the given {@link Scope}.
     * 
     * @param scope
     *            The scope to execute in
     * @param program
     *            The {@link AstNode} to execute
     * 
     * @return The result of the execution
     */
    public static VariableLocation execute(Scope scope, AstNode program) {
        return (new LocationEvaluator(scope)).execute(program);
    }

    /**
     * Run the given {@link AstNode} in the given {@link Scope}.
     * 
     * @param scope
     *            The scope to execute in
     * @param observers
     *            The observers to notify during execution
     * @param program
     *            The {@link AstNode} to execute
     * 
     * @return The result of the execution
     */
    public static VariableLocation execute(Scope scope, ExecutionObserver[] observers, AstNode program) {
        return (new LocationEvaluator(scope, observers)).execute(program);
    }

    /**
     * Run the {@link AstNode} in the scope of the {@link LocationEvaluator}
     * 
     * @param program
     *            The {@link AstNode} to execute
     * 
     * @return The result of the execution
     */
    public VariableLocation execute(AstNode program) {
        if (program != null) {
            if (observers == null) {
                return program.accept(this);
            } else {
                for (ExecutionObserver observer : observers) {
                    observer.beforeNodeLocalization(program, scope);
                }
                VariableLocation result = program.accept(this);
                for (ExecutionObserver observer : observers) {
                    observer.afterNodeLocalization(program, result, scope);
                }
                return result;
            }
        } else {
            return null;
        }
    }

    @Override
    public VariableLocation visit(AstInteger ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstReal ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstString ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstCharacter ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstAddition ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstSubtract ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstMultiply ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstDivide ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstRemainder ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstShiftLeft ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstShiftRight ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstEqual ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstNotEqual ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstLessThan ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstLessOrEqual ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstMoreThan ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstMoreOrEqual ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstBitwiseAnd ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstBitwiseOr ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstBitwiseXor ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstAnd ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstOr ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstPositive ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstNegative ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstBitwiseNot ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstNot ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstReturn ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstClass ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstFunction ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstBlock ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstSequence ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstAssignment ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstSymbol ast) {
        return scope.getVariableLocation(ast.getName());
    }

    @Override
    public VariableLocation visit(AstMember ast) {
        UserObject left = Executor.execute(scope, observers, ast.getOperand());
        if (left == null) {
            throw new InterpreterException(ast.getOperand().getPosition(), "Can not access member of undefined");
        } else {
            return left.getMemberScope().getVariableLocation(ast.getName());
        }
    }

    @Override
    public VariableLocation visit(AstCall ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstIndex ast) {
        UserObject left = Executor.execute(scope, observers, ast.getLeft());
        if (left instanceof UserArray) {
            UserObject right = Executor.execute(scope, observers, ast.getRight());
            if (right instanceof UserInteger) {
                UserArray array = (UserArray) left;
                int index = ((UserInteger) right).getValue().intValue();
                return value -> {
                    if (index < 0 || index >= array.getLength()) {
                        throw new InterpreterException(ast.getPosition(), "Index out of bounds");
                    } else {
                        array.putValueAt(index, value);
                    }
                };
            } else {
                throw new InterpreterException(ast.getRight().getPosition(), "Index is not an integer");
            }
        } else {
            throw new InterpreterException(ast.getLeft().getPosition(), "Value is not an array");
        }
    }

    @Override
    public VariableLocation visit(AstNew ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstArray ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstIfElse ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstFor ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstForIn ast) {
        return null;
    }

    @Override
    public VariableLocation visit(AstInitializer ast) {
        return null;
    }
}
