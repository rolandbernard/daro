package daro.lang.interpreter;

import java.math.BigInteger;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import daro.lang.ast.*;
import daro.lang.values.*;

/**
 * This class implements a simple scope. A scope is a collection of variables (with names and
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
        UserObject value = program.accept(this);
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstFunction ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstBlock ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstAssignment ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstSymbol ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstMember ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstCall ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstIndex ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstNew ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstArray ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstIfElse ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstFor ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstForIn ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstInitializer ast) {
        // TODO Auto-generated method stub
        return null;
    }


}
