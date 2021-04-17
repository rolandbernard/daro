package daro.lang.ast;

/**
 * Class representing an ast node for code blocks. e.g. {@code { ... }}
 * 
 * @author Roland Bernard
 */
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

