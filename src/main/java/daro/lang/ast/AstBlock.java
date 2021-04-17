package daro.lang.ast;

public class AstBlock extends AstNode {
    private final AstNode[] content;

    public AstBlock(Position position, AstNode[] content) {
        super(position);
        this.content = content;
    }

    public AstNode[] getContent() {
        return content;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

