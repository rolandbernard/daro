package daro.lang.ast;

/**
 * Class representing an ast node for a symbol. e.g. {@code foo}
 * 
 * @author Roland Bernard
 */
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

