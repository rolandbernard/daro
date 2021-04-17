package daro.lang.ast;

public class AstReal extends AstNode {
    private final double value;

    public AstReal(Position position, double value) {
        super(position);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

