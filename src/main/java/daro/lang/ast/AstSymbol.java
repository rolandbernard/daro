package daro.lang.ast;

import java.util.Objects;

/**
 * Class representing an ast node for a symbol. e.g. {@code foo}
 * 
 * @author Roland Bernard
 */
public final class AstSymbol extends AstNode {
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

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstSymbol) {
            AstSymbol node = (AstSymbol)obj;
            return Objects.equals(name, node.getName());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}

