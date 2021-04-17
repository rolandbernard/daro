package daro.lang.ast;

public interface Visitor {

    public void visit(AstInteger ast);

    public void visit(AstReal ast);

    public void visit(AstString ast);

    public void visit(AstCharacter ast);

    public void visit(AstAddition ast);

    public void visit(AstSubtract ast);

    public void visit(AstMultiply ast);

    public void visit(AstDivide ast);

    public void visit(AstRemainder ast);

    public void visit(AstShiftLeft ast);

    public void visit(AstShiftRight ast);

    public void visit(AstEqual ast);

    public void visit(AstNotEqual ast);

    public void visit(AstLessThan ast);

    public void visit(AstLessOrEqual ast);

    public void visit(AstMoreThan ast);

    public void visit(AstMoreOrEqual ast);

    public void visit(AstBitwiseAnd ast);

    public void visit(AstBitwiseOr ast);

    public void visit(AstBitwiseXor ast);

    public void visit(AstAnd ast);

    public void visit(AstOr ast);

    public void visit(AstPositive ast);

    public void visit(AstNegative ast);

    public void visit(AstBitwiseNot ast);

    public void visit(AstNot ast);

    public void visit(AstReturn ast);

    public void visit(AstClass ast);

    public void visit(AstFunction ast);

    public void visit(AstBlock ast);

    public void visit(AstAssignment ast);

    public void visit(AstDefinition ast);

    public void visit(AstSymbol ast);

    public void visit(AstMember ast);

    public void visit(AstCall ast);

    public void visit(AstIndex ast);

    public void visit(AstNew ast);

    public void visit(AstArray ast);

    public void visit(AstIfElse ast);

    public void visit(AstFor ast);

    public void visit(AstForIn ast);

    public void visit(AstInitializer ast);


}

