package daro.lang.ast;

/**
 * This interface must be implemented by all Classes that want to visit ast
 * nodes ({@link AstNode}) using the AstNode.accept() method. It contains
 * methods for visiting any of the non abstract descendent classes of
 * {@link AstNode}.
 *
 * @author Roland Bernard
 */
public interface Visitor<T> {

    public T visit(AstInteger ast);

    public T visit(AstReal ast);

    public T visit(AstString ast);

    public T visit(AstCharacter ast);

    public T visit(AstAddition ast);

    public T visit(AstSubtract ast);

    public T visit(AstMultiply ast);

    public T visit(AstDivide ast);

    public T visit(AstRemainder ast);

    public T visit(AstShiftLeft ast);

    public T visit(AstShiftRight ast);

    public T visit(AstEqual ast);

    public T visit(AstNotEqual ast);

    public T visit(AstLessThan ast);

    public T visit(AstLessOrEqual ast);

    public T visit(AstMoreThan ast);

    public T visit(AstMoreOrEqual ast);

    public T visit(AstBitwiseAnd ast);

    public T visit(AstBitwiseOr ast);

    public T visit(AstBitwiseXor ast);

    public T visit(AstAnd ast);

    public T visit(AstOr ast);

    public T visit(AstPositive ast);

    public T visit(AstNegative ast);

    public T visit(AstBitwiseNot ast);

    public T visit(AstNot ast);

    public T visit(AstReturn ast);

    public T visit(AstClass ast);

    public T visit(AstFunction ast);

    public T visit(AstBlock ast);

    public T visit(AstSequence ast);

    public T visit(AstAssignment ast);

    public T visit(AstSymbol ast);

    public T visit(AstMember ast);

    public T visit(AstCall ast);

    public T visit(AstIndex ast);

    public T visit(AstNew ast);

    public T visit(AstArray ast);

    public T visit(AstIfElse ast);

    public T visit(AstFor ast);

    public T visit(AstForIn ast);

    public T visit(AstInitializer ast);

    public T visit(AstPower ast);

    public T visit(AstUse ast);

    public T visit(AstFrom ast);

    public T visit(AstMatch ast);

    public T visit(AstMatchCase ast);
}
