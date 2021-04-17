package daro.lang.ast;

public class AstInitializer extends AstNode {
    private final AstNode[] values;

    public AstInitializer(Position position, AstNode[] values) {
        super(position);
        this.values = values;
    }

    public AstNode[] getValues() {
        return values;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}

