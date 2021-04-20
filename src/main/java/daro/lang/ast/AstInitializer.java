package daro.lang.ast;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class representing an ast node for a initializer list. e.g. {@code {{foo = {1, 2, 3}, bar = 4}, null}}
 * 
 * @author Roland Bernard
 */
public final class AstInitializer extends AstNode {
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

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstInitializer) {
            AstInitializer node = (AstInitializer)obj;
            return Arrays.equals(values, node.getValues());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "("
            + this.getClass().getSimpleName() + " "
            + Arrays.stream(values).map(String::valueOf).collect(Collectors.joining(" "))
            + ")";
    }
}

