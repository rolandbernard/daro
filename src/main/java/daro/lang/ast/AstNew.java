package daro.lang.ast;

public class AstNew extends AstNode {
    private final AstNode type;
    private final AstInitializer initializer;

    public AstNew(Position position, AstNode type, AstInitializer initializer) {
        super(position);
        this.type = type;
        this.initializer = initializer;
    }

    public AstNode getType() {
        return type;
    }

    public AstInitializer getInitialzer() {
        return initializer;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

