package daro.lang.ast;

public class AstFor extends AstNode {
    private final AstNode condition;
    private final AstNode block;

    public AstFor(Position position, AstNode condition, AstNode block) {
        super(position);
        this.condition = condition;
        this.block = block;
    }

    public AstNode getCondition() {
        return condition;
    }

    public AstNode getBlock() {
        return block;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

