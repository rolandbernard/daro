package daro.lang.ast;

public class AstFunction extends AstNode {
    private final String name;
    private final AstDefinition[] parameters;
    private final AstBlock body;

    public AstFunction(Position position, String name, AstDefinition[] parameters, AstBlock body) {
        super(position);
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public AstDefinition[] getParameters() {
        return parameters;
    }

    public AstBlock getBody() {
        return body;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

