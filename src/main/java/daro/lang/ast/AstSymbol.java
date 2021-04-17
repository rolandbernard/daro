package daro.lang.ast;

public class AstSymbol extends AstNode {
    private final String name;

    public AstSymbol(Position position, String name) {
        super(position);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

