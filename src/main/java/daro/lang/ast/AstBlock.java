package daro.lang.ast;

/**
 * Class representing an ast node for code blocks. e.g. {@code { ... }}.
 * A block is basically a sequence that has a seperate scope during execution.
 * 
 * @author Roland Bernard
 */
public final class AstBlock extends AstSequence {

    public AstBlock(Position position, AstNode[] content) {
        super(position, content);
    }

    public AstSequence getSequence() {
        return new AstSequence(getPosition(), getStatemens());
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

