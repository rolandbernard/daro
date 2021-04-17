package daro.lang.ast;

/**
 * Class representing an ast node for a real literal. e.g. {@code 42.17e29}
 * 
 * @author Roland Bernard
 */
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

