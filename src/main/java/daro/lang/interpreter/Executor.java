package daro.lang.interpreter;

import java.math.BigInteger;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import daro.lang.ast.*;
import daro.lang.values.*;

/**
 * This class is used to execute an ast inside a given scope. It is implemented as a tree walking
 * interpreter.
 * 
 * @author Roland Bernard
 */
public class Executor implements Visitor<UserObject> {
    private final Scope scope;

    /**
     * Create a new {@link Executor} for execution in the given scope.
     */
    public Executor(Scope scope) {
        this.scope = scope;
    }

    /**
     * Run the given {@link AstNode} in the given {@link Scope}.
     * @param scope The scope to execute in
     * @param program The {@link AstNode} to execute
     * @return The result of the execution
     */
    public static UserObject execute(Scope scope, AstNode program) {
        return (new Executor(scope)).execute(program);
    }

    /**
     * Run the {@link AstNode} in the scope of the {@link Executor}
     * @param program The {@link AstNode} to execute
     * @return The result of the execution
     */
    public UserObject execute(AstNode program) {
        if (program != null) {
            return program.accept(this);
        } else {
            return null;
        }
    }

    /**
     * This is a utility function that executes the {@link AstNode} and throws a
     * {@link InterpreterException} if the returned value is undefined.
     * @param program The {@link AstNode} to execute
     * @return The result of the execution
     */
    private UserObject require(AstNode program) {
        UserObject value = execute(program);
        if (value != null) {
            return value;
        } else {
            throw new InterpreterException(program.getPosition(), "Value must not be undefined");
        }
    }

    @Override
    public UserObject visit(AstInteger ast) {
        return new UserInteger(ast.getValue());
    }

    @Override
    public UserObject visit(AstReal ast) {
        return new UserReal(ast.getValue());
    }

    @Override
    public UserObject visit(AstString ast) {
        return new UserString(ast.getValue());
    }

    @Override
    public UserObject visit(AstCharacter ast) {
        return new UserInteger(BigInteger.valueOf((long)ast.getValue()));
    }

    /** 
     * This is a utility function for execution of binary functions with different operations for
     * different types of objects.
     * @param integer The function to execute for integers
     * @param number The function to execute for numbers
     * @param all The function to execute for all other objects
     * @return The result of the operation
     */
    private UserObject executeBinary(
        AstBinaryNode ast, BiFunction<UserInteger, UserInteger, UserObject> integer,
        BiFunction<UserNumber, UserNumber, UserObject> number, BinaryOperator<UserObject> all
    ) {
        UserObject left = require(ast.getLeft());
        UserObject right = require(ast.getRight());
        if (integer != null && left instanceof UserInteger && right instanceof UserInteger) {
            UserInteger a = (UserInteger)left;
            UserInteger b = (UserInteger)right;
            return integer.apply(a, b);
        } else if (number != null && left instanceof UserNumber && right instanceof UserNumber) {
            UserNumber a = (UserNumber)left;
            UserNumber b = (UserNumber)right;
            return number.apply(a, b);
        } else if (all != null) {
            return all.apply(left, right);
        } else {
            throw new InterpreterException(
                ast.getPosition(),
                "Objects of types `" + left.getType().toString() + "` and `"
                + right.getType().toString() + "` do not support this operation."
            );
        }
    }

    @Override
    public UserObject visit(AstAddition ast) {
        return executeBinary(ast,
            (a, b) -> new UserInteger(a.getValue().add(b.getValue())),
            (a, b) -> new UserReal(a.doubleValue() + b.doubleValue()),
            (a, b) -> new UserString(a.toString() + b.toString())
        );
    }

    @Override
    public UserObject visit(AstSubtract ast) {
        return executeBinary(ast,
            (a, b) -> new UserInteger(a.getValue().subtract(b.getValue())),
            (a, b) -> new UserReal(a.doubleValue() - b.doubleValue()),
            null
        );
    }

    @Override
    public UserObject visit(AstMultiply ast) {
        return executeBinary(ast,
            (a, b) -> new UserInteger(a.getValue().multiply(b.getValue())),
            (a, b) -> new UserReal(a.doubleValue() * b.doubleValue()),
            null
        );
    }

    @Override
    public UserObject visit(AstDivide ast) {
        return executeBinary(ast,
            (a, b) -> new UserInteger(a.getValue().divide(b.getValue())),
            (a, b) -> new UserReal(a.doubleValue() / b.doubleValue()),
            null
        );
    }

    @Override
    public UserObject visit(AstRemainder ast) {
        return executeBinary(ast,
            (a, b) -> new UserInteger(a.getValue().remainder(b.getValue())),
            (a, b) -> new UserReal(a.doubleValue() % b.doubleValue()),
            null
        );
    }

    @Override
    public UserObject visit(AstShiftLeft ast) {
        return executeBinary(ast,
            (a, b) -> new UserInteger(a.getValue().shiftLeft(b.getValue().intValue())),
            null, null
        );
    }

    @Override
    public UserObject visit(AstShiftRight ast) {
        return executeBinary(ast,
            (a, b) -> new UserInteger(a.getValue().shiftRight(b.getValue().intValue())),
            null, null
        );
    }

    @Override
    public UserObject visit(AstEqual ast) {
        return executeBinary(ast, null, null,
            (a, b) -> new UserBoolean(a.equals(b))
        );
    }

    @Override
    public UserObject visit(AstNotEqual ast) {
        return executeBinary(ast, null, null,
            (a, b) -> new UserBoolean(!a.equals(b))
        );
    }

    @Override
    public UserObject visit(AstLessThan ast) {
        return executeBinary(ast,
            (a, b) -> new UserBoolean(a.getValue().compareTo(b.getValue()) < 0),
            (a, b) -> new UserBoolean(a.doubleValue() < b.doubleValue()),
            (a, b) -> new UserBoolean(a.toString().compareTo(b.toString()) < 0)
        );
    }

    @Override
    public UserObject visit(AstLessOrEqual ast) {
        return executeBinary(ast,
            (a, b) -> new UserBoolean(a.getValue().compareTo(b.getValue()) <= 0),
            (a, b) -> new UserBoolean(a.doubleValue() <= b.doubleValue()),
            (a, b) -> new UserBoolean(a.toString().compareTo(b.toString()) <= 0)
        );
    }

    @Override
    public UserObject visit(AstMoreThan ast) {
        return executeBinary(ast,
            (a, b) -> new UserBoolean(a.getValue().compareTo(b.getValue()) > 0),
            (a, b) -> new UserBoolean(a.doubleValue() > b.doubleValue()),
            (a, b) -> new UserBoolean(a.toString().compareTo(b.toString()) > 0)
        );
    }

    @Override
    public UserObject visit(AstMoreOrEqual ast) {
        return executeBinary(ast,
            (a, b) -> new UserBoolean(a.getValue().compareTo(b.getValue()) >= 0),
            (a, b) -> new UserBoolean(a.doubleValue() >= b.doubleValue()),
            (a, b) -> new UserBoolean(a.toString().compareTo(b.toString()) >= 0)
        );
    }

    @Override
    public UserObject visit(AstBitwiseAnd ast) {
        return executeBinary(ast,
            (a, b) -> new UserInteger(a.getValue().and(b.getValue())),
            null, null
        );
    }

    @Override
    public UserObject visit(AstBitwiseOr ast) {
        return executeBinary(ast,
            (a, b) -> new UserInteger(a.getValue().or(b.getValue())),
            null, null
        );
    }

    @Override
    public UserObject visit(AstBitwiseXor ast) {
        return executeBinary(ast,
            (a, b) -> new UserInteger(a.getValue().xor(b.getValue())),
            null, null
        );
    }

    @Override
    public UserObject visit(AstAnd ast) {
        return new UserBoolean(require(ast.getLeft()).isTrue() && require(ast.getRight()).isTrue());
    }

    @Override
    public UserObject visit(AstOr ast) {
        return new UserBoolean(require(ast.getLeft()).isTrue() || require(ast.getRight()).isTrue());
    }

    /** 
     * This is a utility function for execution of unary functions with different operations for
     * different types of objects.
     * @param integer The function to execute for integers
     * @param number The function to execute for numbers
     * @param all The function to execute for all other objects
     * @return The result of the operation
     */
    private UserObject executeUnary(
        AstUnaryNode ast, Function<UserInteger, ? extends UserObject> integer,
        Function<UserNumber, ? extends UserObject> number, UnaryOperator<UserObject> all
    ) {
        UserObject value = require(ast.getOperand());
        if (integer != null && value instanceof UserInteger) {
            UserInteger a = (UserInteger)value;
            return integer.apply(a);
        } else if (number != null && value instanceof UserNumber) {
            UserNumber a = (UserNumber)value;
            return number.apply(a);
        } else if (all != null) {
            return all.apply(value);
        } else {
            throw new InterpreterException(
                ast.getPosition(),
                "Objects of type `" + value.getType().toString() + "` do not support this operation."
            );
        }
    }

    @Override
    public UserObject visit(AstPositive ast) {
        return executeUnary(ast, Function.identity(), Function.identity(), null);
    }

    @Override
    public UserObject visit(AstNegative ast) {
        return executeUnary(ast,
            a -> new UserInteger(a.getValue().negate()),
            a -> new UserReal(-a.doubleValue()),
            null
        );
    }

    @Override
    public UserObject visit(AstBitwiseNot ast) {
        return executeUnary(ast,
            a -> new UserInteger(a.getValue().not()),
            null, null
        );
    }

    @Override
    public UserObject visit(AstNot ast) {
        return executeUnary(ast, null, null,
            a -> new UserBoolean(!a.isTrue())
        );
    }

    @Override
    public UserObject visit(AstReturn ast) {
        if (ast.getOperand() == null) {
            throw new ReturnException(ast.getPosition(), null);
        } else {
            throw new ReturnException(ast.getPosition(), require(ast.getOperand()));
        }
    }

    @Override
    public UserObject visit(AstClass ast) {
        UserTypeClass value = new UserTypeClass(scope, ast);
        if (ast.getName() != null) {
            if (scope instanceof BlockScope) {
                ((BlockScope)scope).forceNewVariable(ast.getName(), value);
            } else {
                throw new InterpreterException(ast.getPosition(), "The surrounding scope does not support class definitions");
            }
        }
        return value;
    }

    @Override
    public UserObject visit(AstFunction ast) {
        UserAstFunction value = new UserAstFunction(scope, ast);
        if (ast.getName() != null) {
            if (scope instanceof BlockScope) {
                ((BlockScope)scope).forceNewVariable(ast.getName(), value);
            } else {
                throw new InterpreterException(ast.getPosition(), "The surrounding scope does not support function definitions");
            }
        }
        return value;
    }

    @Override
    public UserObject visit(AstBlock ast) {
        BlockScope innerScope = new BlockScope(scope);
        AstSequence sequence = ast.getSequence();
        ScopeInitializer.initialize(innerScope, sequence);
        return execute(innerScope, sequence);
    }

    @Override
    public UserObject visit(AstSequence ast) {
        UserObject value = null;
        for (AstNode statement : ast.getStatemens()) {
            value = execute(statement);
        }
        return value;
    }

    @Override
    public UserObject visit(AstAssignment ast) {
        VariableLocation location = LocationEvaluator.execute(scope, ast.getLeft());
        if (location != null) {
            UserObject value = require(ast.getRight());
            location.storeValue(value);
            return value;
        } else {
            throw new InterpreterException(ast.getLeft().getPosition(), "Expression can not be written to");
        }
    }

    @Override
    public UserObject visit(AstSymbol ast) {
        UserObject value = scope.getVariableValue(ast.getName());
        if (value == null) {
            throw new InterpreterException(ast.getPosition(), "Variable is undefined");
        } else {
            return value;
        }
    }

    @Override
    public UserObject visit(AstMember ast) {
        UserObject left = require(ast.getOperand());
        UserObject value = left.getMemberScope().getVariableValue(ast.getName());
        if (value == null) {
            throw new InterpreterException(ast.getPosition(), "Member variable is undefined");
        } else {
            return value;
        }
    }

    @Override
    public UserObject visit(AstCall ast) {
        UserObject left = require(ast.getFunction());
        if (left instanceof UserFunction) {
            UserFunction function = (UserFunction)left;
            AstNode[] parameters = ast.getParameters();
            if (function.getParamCount() >= 0 && function.getParamCount() != parameters.length) {
                throw new InterpreterException(ast.getFunction().getPosition(), "Worng number of parameters");
            } else {
                UserObject[] parameterValues = new UserObject[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    parameterValues[i] = require(parameters[i]);
                }
                return function.execute(parameterValues);
            }
        } else {
            throw new InterpreterException(ast.getFunction().getPosition(), "Value is not a function");
        }
    }

    @Override
    public UserObject visit(AstIndex ast) {
        UserObject left = require(ast.getLeft());
        if (left instanceof UserArray) {
            UserObject right = require(ast.getRight());
            if (right instanceof UserInteger) {
                UserArray array = (UserArray)left;
                int index = ((UserInteger)right).getValue().intValue();
                if (index < 0 || index >= array.getLength()) {
                    throw new InterpreterException(ast.getPosition(), "Index out of bounds");
                } else {
                    return array.getValueAt(index);
                }
            } else {
                throw new InterpreterException(ast.getRight().getPosition(), "Index is not an integer");
            }
        } else {
            throw new InterpreterException(ast.getLeft().getPosition(), "Value is not an array");
        }
    }

    @Override
    public UserObject visit(AstNew ast) {
        UserObject kind = require(ast.getType());
        if (kind instanceof UserType) {
            UserType type = (UserType)kind;
            if (ast.getInitialzer() != null) {
                return type.instantiate(scope, ast.getInitialzer());
            } else {
                return type.instantiate();
            }
        } else {
            throw new InterpreterException(ast.getType().getPosition(), "Value is not a type");
        }
    }

    @Override
    public UserObject visit(AstArray ast) {
        UserObject value = require(ast.getOperand());
        if (value instanceof UserType) {
            UserType type = (UserType)value;
            return new UserTypeStrictArray(type);
        } else {
            throw new InterpreterException(ast.getOperand().getPosition(), "Value is not a type");
        }
    }

    @Override
    public UserObject visit(AstIfElse ast) {
        if (require(ast.getCondition()).isTrue()) {
            return execute(ast.getIf());
        } else {
            return execute(ast.getElse());
        }
    }

    @Override
    public UserObject visit(AstFor ast) {
        UserObject value = null;
        while (require(ast.getCondition()).isTrue()) {
            value = execute(ast.getBody());
        }
        return value;
    }

    @Override
    public UserObject visit(AstForIn ast) {
        BlockScope innerScope = new BlockScope(scope);
        UserObject value = require(ast.getList());
        if (value instanceof UserArray) {
            UserObject ret = null;
            UserArray array = (UserArray)value;
            for (int i = 0; i < array.getLength(); i++) {
                UserObject item = array.getValueAt(i);
                innerScope.forceNewVariable(ast.getVariable().getName(), item);
                ret = execute(innerScope, ast.getBody());
            }
            return ret;
        } else {
            throw new InterpreterException(ast.getList().getPosition(), "Value is not an array");
        }
    }

    @Override
    public UserObject visit(AstInitializer ast) {
        throw new InterpreterException(ast.getPosition(), "Execution error");
    }
}
