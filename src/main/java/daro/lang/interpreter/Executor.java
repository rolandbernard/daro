package daro.lang.interpreter;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import daro.lang.ast.*;
import daro.lang.parser.Parser;
import daro.lang.parser.ParsingException;
import daro.lang.values.*;

/**
 * This class is used to execute an ast inside a given scope. It is implemented
 * as a tree walking interpreter.
 * 
 * @author Roland Bernard
 */
public class Executor implements Visitor<DaroObject> {
    private final ExecutionContext context;

    /**
     * Create a new {@link Executor} for execution in the given
     * {@link ExecutionContext}.
     * 
     * @param context The context to execute in
     */
    public Executor(ExecutionContext context) {
        this.context = context;
    }

    /**
     * Run the given {@link AstNode} in the given {@link ExecutionContext}.
     * 
     * @param context The context to execute in
     * @param program The {@link AstNode} to execute
     * @return The result of the execution
     */
    public static DaroObject execute(ExecutionContext context, AstNode program) {
        return (new Executor(context)).execute(program);
    }

    /**
     * Run the given file in the given {@link ExecutionContext}.
     * 
     * @param context The context to execute in
     * @param file    The file to execute
     * @return The result of the execution
     */
    public static DaroModule executeFile(ExecutionContext context, Path file) {
        return (new Executor(context)).executeFile(file);
    }

    /**
     * Run the {@link AstNode} in the scope of the {@link Executor}
     * 
     * @param program The {@link AstNode} to execute
     * @return The result of the execution
     */
    public DaroObject execute(AstNode program) {
        if (program != null) {
            try {
                ExecutionObserver[] observers = context.getObservers();
                if (observers == null) {
                    return program.accept(this);
                } else {
                    Scope scope = context.getScope();
                    for (ExecutionObserver observer : observers) {
                        observer.beforeNodeExecution(program, scope);
                    }
                    DaroObject result = program.accept(this);
                    for (ExecutionObserver observer : observers) {
                        observer.afterNodeExecution(program, result, scope);
                    }
                    return result;
                }
            } catch (ParsingException error) {
                throw error;
            } catch (InterpreterException error) {
                if (error.getPosition() == null) {
                    // Some exceptions are thrown in locations without positional information.
                    throw new InterpreterException(program.getPosition(), error.getMessage());
                } else {
                    throw error;
                }
            } catch (Exception error) {
                // All other exceptions should be converted to {@link InterpreterException}
                InterpreterException exception = new InterpreterException(program.getPosition(), error.getMessage());
                exception.initCause(error);
                throw exception;
            }
        } else {
            return null;
        }
    }

    /**
     * This is a utility function that executes the {@link AstNode} and throws a
     * {@link InterpreterException} if the returned value is undefined.
     * 
     * @param program The {@link AstNode} to execute
     * @return The result of the execution
     */
    private DaroObject require(AstNode program) {
        DaroObject value = execute(program);
        if (value != null) {
            return value;
        } else {
            throw new InterpreterException(program.getPosition(), "Value must not be undefined");
        }
    }

    @Override
    public DaroObject visit(AstInteger ast) {
        return new DaroInteger(ast.getValue());
    }

    @Override
    public DaroObject visit(AstReal ast) {
        return new DaroReal(ast.getValue());
    }

    @Override
    public DaroObject visit(AstString ast) {
        return new DaroString(ast.getValue());
    }

    @Override
    public DaroObject visit(AstCharacter ast) {
        return new DaroInteger(BigInteger.valueOf((long)ast.getValue()));
    }

    /**
     * This is a utility function for execution of binary functions with different
     * operations for different types of objects.
     * 
     * @param ast     The ast to execute
     * @param integer The function to execute for integers
     * @param number  The function to execute for numbers
     * @param all     The function to execute for all other objects
     * @return The result of the operation
     */
    private DaroObject executeBinary(
        AstBinaryNode ast, BiFunction<DaroInteger, DaroInteger, DaroObject> integer,
        BiFunction<DaroNumber, DaroNumber, DaroObject> number, BinaryOperator<DaroObject> all
    ) {
        DaroObject left = require(ast.getLeft());
        DaroObject right = require(ast.getRight());
        if (integer != null && left instanceof DaroInteger && right instanceof DaroInteger) {
            DaroInteger a = (DaroInteger)left;
            DaroInteger b = (DaroInteger)right;
            return integer.apply(a, b);
        } else if (number != null && left instanceof DaroNumber && right instanceof DaroNumber) {
            DaroNumber a = (DaroNumber)left;
            DaroNumber b = (DaroNumber)right;
            return number.apply(a, b);
        } else if (all != null) {
            return all.apply(left, right);
        } else {
            throw new InterpreterException(
                ast.getPosition(),
                "Objects of types `" + left.getType().toString() + "` and `" + right.getType().toString()
                    + "` do not support this operation."
            );
        }
    }

    @Override
    public DaroObject visit(AstAddition ast) {
        return executeBinary(
            ast, (a, b) -> new DaroInteger(a.getValue().add(b.getValue())),
            (a, b) -> new DaroReal(a.doubleValue() + b.doubleValue()),
            (a, b) -> new DaroString(a.toString() + b.toString())
        );
    }

    @Override
    public DaroObject visit(AstSubtract ast) {
        return executeBinary(
            ast, (a, b) -> new DaroInteger(a.getValue().subtract(b.getValue())),
            (a, b) -> new DaroReal(a.doubleValue() - b.doubleValue()), null
        );
    }

    @Override
    public DaroObject visit(AstMultiply ast) {
        return executeBinary(
            ast, (a, b) -> new DaroInteger(a.getValue().multiply(b.getValue())),
            (a, b) -> new DaroReal(a.doubleValue() * b.doubleValue()), null
        );
    }

    @Override
    public DaroObject visit(AstDivide ast) {
        return executeBinary(
            ast, (a, b) -> new DaroInteger(a.getValue().divide(b.getValue())),
            (a, b) -> new DaroReal(a.doubleValue() / b.doubleValue()), null
        );
    }

    @Override
    public DaroObject visit(AstRemainder ast) {
        return executeBinary(
            ast, (a, b) -> new DaroInteger(a.getValue().remainder(b.getValue())),
            (a, b) -> new DaroReal(a.doubleValue() % b.doubleValue()), null
        );
    }

    @Override
    public DaroObject visit(AstShiftLeft ast) {
        return executeBinary(
            ast, (a, b) -> new DaroInteger(a.getValue().shiftLeft(b.getValue().intValue())), null, null
        );
    }

    @Override
    public DaroObject visit(AstShiftRight ast) {
        return executeBinary(
            ast, (a, b) -> new DaroInteger(a.getValue().shiftRight(b.getValue().intValue())), null, null
        );
    }

    @Override
    public DaroObject visit(AstEqual ast) {
        return executeBinary(
            ast, (a, b) -> new DaroBoolean(a.equals(b)), (a, b) -> new DaroBoolean(a.doubleValue() == b.doubleValue()),
            (a, b) -> new DaroBoolean(a.equals(b))
        );
    }

    @Override
    public DaroObject visit(AstNotEqual ast) {
        return executeBinary(ast, null, null, (a, b) -> new DaroBoolean(!a.equals(b)));
    }

    @Override
    public DaroObject visit(AstLessThan ast) {
        return executeBinary(
            ast, (a, b) -> new DaroBoolean(a.getValue().compareTo(b.getValue()) < 0),
            (a, b) -> new DaroBoolean(a.doubleValue() < b.doubleValue()),
            (a, b) -> new DaroBoolean(a.toString().compareTo(b.toString()) < 0)
        );
    }

    @Override
    public DaroObject visit(AstLessOrEqual ast) {
        return executeBinary(
            ast, (a, b) -> new DaroBoolean(a.getValue().compareTo(b.getValue()) <= 0),
            (a, b) -> new DaroBoolean(a.doubleValue() <= b.doubleValue()),
            (a, b) -> new DaroBoolean(a.toString().compareTo(b.toString()) <= 0)
        );
    }

    @Override
    public DaroObject visit(AstMoreThan ast) {
        return executeBinary(
            ast, (a, b) -> new DaroBoolean(a.getValue().compareTo(b.getValue()) > 0),
            (a, b) -> new DaroBoolean(a.doubleValue() > b.doubleValue()),
            (a, b) -> new DaroBoolean(a.toString().compareTo(b.toString()) > 0)
        );
    }

    @Override
    public DaroObject visit(AstMoreOrEqual ast) {
        return executeBinary(
            ast, (a, b) -> new DaroBoolean(a.getValue().compareTo(b.getValue()) >= 0),
            (a, b) -> new DaroBoolean(a.doubleValue() >= b.doubleValue()),
            (a, b) -> new DaroBoolean(a.toString().compareTo(b.toString()) >= 0)
        );
    }

    @Override
    public DaroObject visit(AstBitwiseAnd ast) {
        return executeBinary(ast, (a, b) -> new DaroInteger(a.getValue().and(b.getValue())), null, null);
    }

    @Override
    public DaroObject visit(AstBitwiseOr ast) {
        return executeBinary(ast, (a, b) -> new DaroInteger(a.getValue().or(b.getValue())), null, null);
    }

    @Override
    public DaroObject visit(AstBitwiseXor ast) {
        return executeBinary(ast, (a, b) -> new DaroInteger(a.getValue().xor(b.getValue())), null, null);
    }

    @Override
    public DaroObject visit(AstAnd ast) {
        return new DaroBoolean(require(ast.getLeft()).isTrue() && require(ast.getRight()).isTrue());
    }

    @Override
    public DaroObject visit(AstOr ast) {
        return new DaroBoolean(require(ast.getLeft()).isTrue() || require(ast.getRight()).isTrue());
    }

    /**
     * This is a utility function for execution of unary functions with different
     * operations for different types of objects.
     * 
     * @param ast     The ast to execute
     * @param integer The function to execute for integers
     * @param number  The function to execute for numbers
     * @param all     The function to execute for all other objects
     * @return The result of the operation
     */
    private DaroObject executeUnary(
        AstUnaryNode ast, Function<DaroInteger, ? extends DaroObject> integer,
        Function<DaroNumber, ? extends DaroObject> number, UnaryOperator<DaroObject> all
    ) {
        DaroObject value = require(ast.getOperand());
        if (integer != null && value instanceof DaroInteger) {
            DaroInteger a = (DaroInteger)value;
            return integer.apply(a);
        } else if (number != null && value instanceof DaroNumber) {
            DaroNumber a = (DaroNumber)value;
            return number.apply(a);
        } else if (all != null) {
            return all.apply(value);
        } else {
            throw new InterpreterException(
                ast.getPosition(), "Objects of type `" + value.getType().toString() + "` do not support this operation."
            );
        }
    }

    @Override
    public DaroObject visit(AstPositive ast) {
        return executeUnary(ast, Function.identity(), Function.identity(), null);
    }

    @Override
    public DaroObject visit(AstNegative ast) {
        return executeUnary(
            ast, a -> new DaroInteger(a.getValue().negate()), a -> new DaroReal(-a.doubleValue()), null
        );
    }

    @Override
    public DaroObject visit(AstBitwiseNot ast) {
        return executeUnary(ast, a -> new DaroInteger(a.getValue().not()), null, null);
    }

    @Override
    public DaroObject visit(AstNot ast) {
        return executeUnary(ast, null, null, a -> new DaroBoolean(!a.isTrue()));
    }

    @Override
    public DaroObject visit(AstReturn ast) {
        if (ast.getOperand() == null) {
            throw new ReturnException(ast.getPosition(), null);
        } else {
            throw new ReturnException(ast.getPosition(), require(ast.getOperand()));
        }
    }

    @Override
    public DaroObject visit(AstClass ast) {
        DaroTypeClass value = new DaroTypeClass(context.getScope(), ast);
        if (ast.getName() != null) {
            context.getScope().newVariableInFinal(ast.getName(), value);
        }
        return value;
    }

    @Override
    public DaroObject visit(AstFunction ast) {
        DaroAstFunction value = new DaroAstFunction(context.getScope(), ast);
        if (ast.getName() != null) {
            context.getScope().newVariableInFinal(ast.getName(), value);
        }
        return value;
    }

    @Override
    public DaroObject visit(AstBlock ast) {
        BlockScope innerScope = new BlockScope(context.getScope());
        AstSequence sequence = ast.getSequence();
        ScopeInitializer.initialize(innerScope, sequence);
        return execute(context.forScope(innerScope), sequence);
    }

    @Override
    public DaroObject visit(AstSequence ast) {
        DaroObject value = null;
        for (AstNode statement : ast.getStatemens()) {
            value = execute(statement);
        }
        return value;
    }

    @Override
    public DaroObject visit(AstAssignment ast) {
        VariableLocation location = LocationEvaluator.execute(context, ast.getLeft());
        if (location != null) {
            DaroObject value = require(ast.getRight());
            location.storeValue(value);
            return value;
        } else {
            throw new InterpreterException(ast.getLeft().getPosition(), "Expression can not be written to");
        }
    }

    @Override
    public DaroObject visit(AstSymbol ast) {
        DaroObject value = context.getScope().getVariableValue(ast.getName());
        if (value == null) {
            throw new InterpreterException(ast.getPosition(), "Variable `" + ast.getName() + "` is undefined");
        } else {
            return value;
        }
    }

    @Override
    public DaroObject visit(AstMember ast) {
        DaroObject left = require(ast.getOperand());
        DaroObject value = left.getMemberScope().getVariableValue(ast.getName());
        if (value == null) {
            throw new InterpreterException(ast.getPosition(), "Member variable `" + ast.getName() + "` is undefined");
        } else {
            return value;
        }
    }

    @Override
    public DaroObject visit(AstCall ast) {
        DaroObject left = require(ast.getFunction());
        if (left instanceof DaroFunction) {
            DaroFunction function = (DaroFunction)left;
            AstNode[] parameters = ast.getParameters();
            if (!function.allowsParamCount(parameters.length)) {
                throw new InterpreterException(ast.getFunction().getPosition(), "Wrong number of parameters");
            } else {
                DaroObject[] parameterValues = new DaroObject[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    parameterValues[i] = require(parameters[i]);
                }
                return function.execute(parameterValues, context);
            }
        } else {
            throw new InterpreterException(ast.getFunction().getPosition(), "Value is not a function");
        }
    }

    @Override
    public DaroObject visit(AstIndex ast) {
        DaroObject left = require(ast.getLeft());
        if (left instanceof DaroArray) {
            DaroObject right = require(ast.getRight());
            if (right instanceof DaroInteger) {
                DaroArray array = (DaroArray)left;
                int index = ((DaroInteger)right).getValue().intValue();
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
    public DaroObject visit(AstNew ast) {
        DaroObject kind = require(ast.getType());
        if (kind instanceof DaroType) {
            DaroType type = (DaroType)kind;
            if (ast.getInitialzer() != null) {
                return type.instantiate(context, ast.getInitialzer());
            } else {
                return type.instantiate(context);
            }
        } else {
            throw new InterpreterException(ast.getType().getPosition(), "Value is not a type");
        }
    }

    @Override
    public DaroObject visit(AstArray ast) {
        DaroObject value = require(ast.getRight());
        if (value instanceof DaroType) {
            DaroType type = (DaroType)value;
            DaroObject size = execute(ast.getLeft());
            if (size == null) {
                return new DaroTypeStrictArray(type);
            } else if (size instanceof DaroInteger) {
                DaroInteger integer = (DaroInteger)size;
                return new DaroTypeStrictArray(integer.getValue().intValue(), type);
            } else {
                throw new InterpreterException(ast.getLeft().getPosition(), "Size is not an integer");
            }
        } else {
            throw new InterpreterException(ast.getRight().getPosition(), "Value is not a type");
        }
    }

    @Override
    public DaroObject visit(AstIfElse ast) {
        if (require(ast.getCondition()).isTrue()) {
            return execute(ast.getIf());
        } else {
            return execute(ast.getElse());
        }
    }

    @Override
    public DaroObject visit(AstFor ast) {
        DaroObject value = null;
        while (require(ast.getCondition()).isTrue()) {
            value = execute(ast.getBody());
        }
        return value;
    }

    @Override
    public DaroObject visit(AstForIn ast) {
        BlockScope innerScope = new BlockScope(context.getScope());
        DaroObject value = require(ast.getList());
        if (value instanceof DaroArray) {
            DaroObject ret = null;
            DaroArray array = (DaroArray)value;
            for (int i = 0; i < array.getLength(); i++) {
                DaroObject item = array.getValueAt(i);
                innerScope.newVariableInFinal(ast.getVariable().getName(), item);
                ret = execute(context.forScope(innerScope), ast.getBody());
            }
            return ret;
        } else {
            throw new InterpreterException(ast.getList().getPosition(), "Value is not an array");
        }
    }

    @Override
    public DaroObject visit(AstInitializer ast) {
        throw new InterpreterException(ast.getPosition(), "Execution error");
    }

    @Override
    public DaroObject visit(AstPower ast) {
        return executeBinary(
            ast, (a, b) -> new DaroInteger(a.getValue().pow(b.getValue().intValue())),
            (a, b) -> new DaroReal(Math.pow(a.doubleValue(), b.doubleValue())), null
        );
    }

    @Override
    public DaroObject visit(AstUse ast) {
        DaroObject value = require(ast.getOperand());
        Scope scope = context.getScope();
        if (scope instanceof AbstractScope) {
            AbstractScope cast = (AbstractScope)scope;
            cast.addParent(value.getMemberScope());
            return null;
        } else {
            throw new InterpreterException(ast.getPosition(), "Use can not be used in the surrounding context");
        }
    }

    /**
     * This is a utility function that searches for a file inside all of the paths
     * given by search in order an then as a last resort relative to the program
     * execution. The function will return the absolute path of whatever file it
     * finds.
     *
     * @param file   The file to search
     * @param search The locations to search in
     * @return The path to the files expected location
     */
    private Path searchForImport(Path file, Path ...search) {
        for (Path location : search) {
            Path possibility = Path.of(location.toString(), file.toString()).toAbsolutePath();
            if (Files.isRegularFile(possibility) && Files.isReadable(possibility)) {
                return possibility;
            }
        }
        return file.toAbsolutePath();
    }

    /**
     * Execute a file in the executors context and return the resulting
     * {@link DaroModule}. This will also add the file as a module in the executors
     * {@link ExecutionContext}.
     *
     * @param file   The file that should be executed
     * @param search The locations to search in
     * @return The daro module resulting from execution
     */
    public DaroModule executeFile(Path file, Path ...search) {
        Path path = searchForImport(file, search);
        Map<Path, DaroModule> modules = context.getModules();
        if (!modules.containsKey(path)) {
            Scope scope = new BlockScope(new RootScope());
            String content;
            try {
                content = Files.readString(path);
            } catch (IOException e) {
                throw new InterpreterException(new Position(file), "Failed to load file");
            }
            DaroModule pack = new DaroModule(scope);
            modules.put(path.normalize(), pack);
            AstNode program = Parser.parseSourceCode(content, path);
            ScopeInitializer.initialize(scope, program);
            execute(context.forScope(scope), program);
        }
        return modules.get(path);
    }

    @Override
    public DaroObject visit(AstFrom ast) {
        DaroObject value = require(ast.getOperand());
        if (value instanceof DaroString) {
            DaroString string = (DaroString)value;
            Path path = Path.of(string.getValue());
            Path importFrom = ast.getPosition().getFile();
            if (importFrom != null && importFrom.getParent() != null) {
                return executeFile(path, importFrom.getParent());
            } else {
                return executeFile(path);
            }
        } else {
            throw new InterpreterException(ast.getPosition(), "Expected a string object");
        }
    }
}
