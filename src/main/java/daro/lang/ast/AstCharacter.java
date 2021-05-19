package daro.lang.ast;

/**
 * Class representing an ast node for a character literal. e.g. {@code 'a'}
 * 
 * @author Roland Bernard
 */
public final class AstCharacter extends AstNode {
    private final char value;

    public AstCharacter(Position position, char value) {
        super(position);
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Character.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstCharacter) {
            AstCharacter node = (AstCharacter) obj;
            return value == node.getValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "'" + value + "'";
    }
}
