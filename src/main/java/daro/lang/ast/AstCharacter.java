package daro.lang.ast;

/**
 * Class representing an ast node for a character literal. e.g. {@code 'a'}
 * 
 * @author Roland Bernard
 */
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

