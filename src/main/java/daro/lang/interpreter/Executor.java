package daro.lang.interpreter;

import daro.lang.ast.*;
import daro.lang.values.UserObject;

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
     * Run the {@link AstNode} in the scope of the {@link Executor}
     * @param program The {@link AstNode} to execute
     * @return The result of the execution
     */
    public UserObject executeAst(AstNode program) {
        return program.accept(this);
    }

    @Override
    public UserObject visit(AstInteger ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstReal ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstString ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstCharacter ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstAddition ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstSubtract ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstMultiply ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstDivide ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstRemainder ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstShiftLeft ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstShiftRight ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstEqual ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstNotEqual ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstLessThan ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstLessOrEqual ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstMoreThan ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstMoreOrEqual ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstBitwiseAnd ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstBitwiseOr ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstBitwiseXor ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstAnd ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstOr ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstPositive ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstNegative ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstBitwiseNot ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstNot ast) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserObject visit(AstReturn ast) {
        // TODO Auto-generated method stub
        return null;
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
