package daro.lang.ast;

public class AstInteger extends AstNode {
    private final long value;

    public AstInteger(Position position, long value) {
        super(position);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

