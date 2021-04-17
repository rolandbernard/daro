package daro.lang.ast;

public class AstClass extends AstNode {
    private final String name;
    private final AstBlock body;

    public AstClass(Position position, String name, AstBlock body) {
        super(position);
        this.name = name;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public AstBlock getBody() {
        return body;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

