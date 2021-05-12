package daro.lang.interpreter;

import daro.lang.ast.*;
import daro.lang.values.*;

/**
 * This class is used to initialize a scope with all the classes and function that it contains. This
 * is necessary to allow calling of functions before they are defined.
 * 
 * @author Roland Bernard
 */
public class ScopeInitializer implements Visitor<Object> {
    private final Scope scope;

    /**
     * Create a new {@link ScopeInitializer} for initializing the given scope.
     */
    public ScopeInitializer(Scope scope) {
        this.scope = scope;
    }

    /**
     * Iniialize using the given {@link AstNode} the the given {@link Scope}.
     * @param scope The scope to initialize
     * @param program The {@link AstNode} to initialize with
     */
    public static void initialize(Scope scope, AstNode program) {
        (new ScopeInitializer(scope)).initialize(program);
    }

    /**
     * Iniialize using the given {@link AstNode}.
     * @param program The {@link AstNode} to initialize with
     */
    public void initialize(AstNode program) {
        if (program != null) {
            program.accept(this);
        }
    }

    @Override
    public Object visit(AstInteger ast) {
        return null;
    }

    @Override
    public Object visit(AstReal ast) {
        return null;
    }

    @Override
    public Object visit(AstString ast) {
        return null;
    }

    @Override
    public Object visit(AstCharacter ast) {
        return null;
    }

    /**
     * Utility function to initialize a binary ast node.
     * @param ast The ast to initialize with
     */
    public void initializeBinary(AstBinaryNode ast) {
        initialize(ast.getLeft());
        initialize(ast.getRight());
    }

    @Override
    public Object visit(AstAddition ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstSubtract ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstMultiply ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstDivide ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstRemainder ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstShiftLeft ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstShiftRight ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstEqual ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstNotEqual ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstLessThan ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstLessOrEqual ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstMoreThan ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstMoreOrEqual ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstBitwiseAnd ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstBitwiseOr ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstBitwiseXor ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstAnd ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstOr ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstPositive ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Object visit(AstNegative ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Object visit(AstBitwiseNot ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Object visit(AstNot ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Object visit(AstReturn ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Object visit(AstClass ast) {
        if (ast.getName() != null) {
            if (scope instanceof BlockScope) {
                UserTypeClass value = new UserTypeClass(scope, ast);
                ((BlockScope)scope).forceNewVariable(ast.getName(), value);
            } else {
                throw new InterpreterException(ast.getPosition(), "The surrounding scope does not support class definitions");
            }
        }
        return null;
    }

    @Override
    public Object visit(AstFunction ast) {
        if (ast.getName() != null) {
            if (scope instanceof BlockScope) {
                UserAstFunction value = new UserAstFunction(scope, ast);
                ((BlockScope)scope).forceNewVariable(ast.getName(), value);
            } else {
                throw new InterpreterException(ast.getPosition(), "The surrounding scope does not support function definitions");
            }
        }
        return null;
    }

    @Override
    public Object visit(AstBlock ast) {
        // Searching stops at scope boundaries
        return null;
    }

    @Override
    public Object visit(AstSequence ast) {
        for (AstNode statement : ast.getStatemens()) {
            initialize(statement);
        }
        return null;
    }

    @Override
    public Object visit(AstAssignment ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstSymbol ast) {
        return null;
    }

    @Override
    public Object visit(AstMember ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Object visit(AstCall ast) {
        initialize(ast.getFunction());
        for (AstNode parameter : ast.getParameters()) {
            initialize(parameter);
        }
        return null;
    }

    @Override
    public Object visit(AstIndex ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Object visit(AstNew ast) {
        initialize(ast.getType());
        initialize(ast.getInitialzer());
        return null;
    }

    @Override
    public Object visit(AstArray ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Object visit(AstIfElse ast) {
        initialize(ast.getCondition());
        initialize(ast.getIf());
        initialize(ast.getElse());
        return null;
    }

    @Override
    public Object visit(AstFor ast) {
        initialize(ast.getCondition());
        initialize(ast.getBody());
        return null;
    }

    @Override
    public Object visit(AstForIn ast) {
        initialize(ast.getVariable());
        initialize(ast.getList());
        initialize(ast.getBody());
        return null;
    }

    @Override
    public Object visit(AstInitializer ast) {
        for (AstNode parameter : ast.getValues()) {
            initialize(parameter);
        }
        return null;
    }
}
