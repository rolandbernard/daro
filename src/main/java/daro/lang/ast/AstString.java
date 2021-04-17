package daro.lang.ast;

public class AstString extends AstNode {
    private final String value;

    public AstString(Position position, String value) {
        super(position);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

