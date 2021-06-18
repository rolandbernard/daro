package daro.lang.interpreter;

import daro.lang.ast.*;
import daro.lang.values.*;

/**
 * This class is used to initialize a scope with all the classes and function
 * that it contains. This is necessary to allow calling of functions before they
 * are defined.
 * 
 * @author Roland Bernard
 */
public class ScopeInitializer implements Visitor<Void> {
    private final Scope scope;

    /**
     * Create a new {@link ScopeInitializer} for initializing the given scope.
     * 
     * @param scope The scope to initialize
     */
    public ScopeInitializer(Scope scope) {
        this.scope = scope;
    }

    /**
     * Initialize using the given {@link AstNode} the the given {@link Scope}.
     * 
     * @param scope   The scope to initialize
     * @param program The {@link AstNode} to initialize with
     */
    public static void initialize(Scope scope, AstNode program) {
        (new ScopeInitializer(scope)).initialize(program);
    }

    /**
     * Initialize using the given {@link AstNode}.
     * 
     * @param program The {@link AstNode} to initialize with
     */
    public void initialize(AstNode program) {
        if (program != null) {
            program.accept(this);
        }
    }

    @Override
    public Void visit(AstInteger ast) {
        return null;
    }

    @Override
    public Void visit(AstReal ast) {
        return null;
    }

    @Override
    public Void visit(AstString ast) {
        return null;
    }

    @Override
    public Void visit(AstCharacter ast) {
        return null;
    }

    /**
     * Utility function to initialize a binary ast node.
     * 
     * @param ast The ast to initialize with
     */
    public void initializeBinary(AstBinaryNode ast) {
        initialize(ast.getLeft());
        initialize(ast.getRight());
    }

    @Override
    public Void visit(AstAddition ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstSubtract ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstMultiply ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstDivide ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstRemainder ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstShiftLeft ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstShiftRight ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstEqual ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstNotEqual ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstLessThan ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstLessOrEqual ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstMoreThan ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstMoreOrEqual ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstBitwiseAnd ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstBitwiseOr ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstBitwiseXor ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstAnd ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstOr ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstPositive ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Void visit(AstNegative ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Void visit(AstBitwiseNot ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Void visit(AstNot ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Void visit(AstReturn ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Void visit(AstClass ast) {
        if (ast.getName() != null) {
            DaroTypeClass value = new DaroTypeClass(scope, ast);
            scope.newVariableInFinal(ast.getName(), value);
        }
        return null;
    }

    @Override
    public Void visit(AstFunction ast) {
        if (ast.getName() != null) {
            DaroAstFunction value = new DaroAstFunction(scope, ast);
            scope.newVariableInFinal(ast.getName(), value);
        }
        return null;
    }

    @Override
    public Void visit(AstBlock ast) {
        // Searching stops at scope boundaries
        return null;
    }

    @Override
    public Void visit(AstSequence ast) {
        for (AstNode statement : ast.getStatements()) {
            initialize(statement);
        }
        return null;
    }

    @Override
    public Void visit(AstAssignment ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstSymbol ast) {
        return null;
    }

    @Override
    public Void visit(AstMember ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Void visit(AstCall ast) {
        initialize(ast.getFunction());
        for (AstNode parameter : ast.getParameters()) {
            initialize(parameter);
        }
        return null;
    }

    @Override
    public Void visit(AstIndex ast) {
        initialize(ast.getArray());
        initialize(ast.getStart());
        initialize(ast.getEnd());
        return null;
    }

    @Override
    public Void visit(AstNew ast) {
        initialize(ast.getType());
        initialize(ast.getInitializer());
        return null;
    }

    @Override
    public Void visit(AstArray ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstIfElse ast) {
        initialize(ast.getCondition());
        initialize(ast.getIf());
        initialize(ast.getElse());
        return null;
    }

    @Override
    public Void visit(AstFor ast) {
        initialize(ast.getCondition());
        initialize(ast.getBody());
        return null;
    }

    @Override
    public Void visit(AstForIn ast) {
        initialize(ast.getVariable());
        initialize(ast.getList());
        initialize(ast.getBody());
        return null;
    }

    @Override
    public Void visit(AstInitializer ast) {
        for (AstNode parameter : ast.getValues()) {
            initialize(parameter);
        }
        return null;
    }

    @Override
    public Void visit(AstPower ast) {
        initializeBinary(ast);
        return null;
    }

    @Override
    public Void visit(AstUse ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Void visit(AstFrom ast) {
        initialize(ast.getOperand());
        return null;
    }

    @Override
    public Void visit(AstMatch ast) {
        initialize(ast.getValue());
        for (AstMatchCase node : ast.getCases()) {
            initialize(node);
        }
        return null;
    }

    @Override
    public Void visit(AstMatchCase ast) {
        if (ast.getValues() != null) {
            for (AstNode node : ast.getValues()) {
                initialize(node);
            }
        }
        initialize(ast.getStatement());
        return null;
    }
}
