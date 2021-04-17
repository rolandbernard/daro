package daro.lang.ast;

public class AstCharacter extends AstNode {
    private final char value;

    public AstCharacter(Position position, char value) {
        super(position);
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

