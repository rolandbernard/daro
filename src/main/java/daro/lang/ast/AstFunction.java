package daro.lang.ast;

/**
 * Class representing an ast node for a function definition. e.g. {@code fn foo( ... ): bar { ... }}
 * 
 * @author Roland Bernard
 */
public class AstFunction extends AstNode {
    private final String name;
    private final AstNode type;
    private final AstDefinition[] parameters;
    private final AstBlock body;

    public AstFunction(Position position, String name, AstNode type, AstDefinition[] parameters, AstBlock body) {
        super(position);
        this.name = name;
        this.type = type;
        this.parameters = parameters;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public AstNode getType() {
        return type;
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

