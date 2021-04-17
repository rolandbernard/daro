package daro.lang.ast;

/**
 * Class representing an ast node for a variable definition. e.g. {@code foo: bar = fizz}
 * 
 * @author Roland Bernard
 */
public class AstDefinition extends AstNode {
    private final String name;
    private final AstNode type;
    private final AstNode value;

    public AstDefinition(Position position, String name, AstNode type, AstNode value) {
        super(position);
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public AstNode getType() {
        return type;
    }

    public AstNode getValue() {
        return value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

