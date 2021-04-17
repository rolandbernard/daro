package daro.lang.ast;

/**
 * Class representing an ast node for a for-in loop. e.g. {@code for foo in bar { ... }}
 * 
 * @author Roland Bernard
 */
public class AstForIn extends AstNode {
    private final AstSymbol variable;
    private final AstNode list;
    private final AstNode block;

    public AstForIn(Position position, AstSymbol variable, AstNode list, AstBlock block) {
        super(position);
        this.variable = variable;
        this.list = list;
        this.block = block;
    }

    public AstSymbol variable() {
        return variable;
    }

    public AstNode getList() {
        return list;
    }

    public AstNode getBlock() {
        return block;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

