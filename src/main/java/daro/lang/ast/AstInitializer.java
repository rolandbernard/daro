package daro.lang.ast;

/**
 * Class representing an ast node for a initializer list. e.g. {@code {{foo = {1, 2, 3}, bar = 4}, null}}
 * 
 * @author Roland Bernard
 */
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

