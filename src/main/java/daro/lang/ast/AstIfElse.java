package daro.lang.ast;

/**
 * Class representing an ast node for a if-else condition. e.g. {@code if foo { ... } else { ... }}
 * 
 * @author Roland Bernard
 */
public class AstIfElse extends AstNode {
    private final AstNode condition;
    private final AstNode ifBlock;
    private final AstNode elseBlock;

    public AstIfElse(Position position, AstNode condition, AstNode ifBlock, AstNode elseBlock) {
        super(position);
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public AstNode getCondition() {
        return condition;
    }

    public AstNode getIfBlock() {
        return ifBlock;
    }

    public AstNode getElseBlock() {
        return elseBlock;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

