package daro.lang.ast;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class representing an ast node for a match statement case. e.g.
 * {@code 1, 2, 3: foo()}
 * 
 * @author Roland Bernard
 */
public final class AstMatchCase extends AstNode {
    private final AstNode[] values;
    private final AstNode statement;

    public AstMatchCase(Position position, AstNode[] values, AstNode statement) {
        super(position);
        this.values = values;
        this.statement = statement;
    }

    public AstNode[] getValues() {
        return values;
    }

    public AstNode getStatement() {
        return statement;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AstNode[] getChildren() {
        AstNode[] ret = Arrays.copyOf(values, values.length + 1);
        ret[values.length] = statement;
        return ret;
    }

    @Override
    public int hashCode() {
        return (997 * Arrays.hashCode(values)) ^ (991 * Objects.hashCode(statement));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstMatchCase) {
            AstMatchCase node = (AstMatchCase)obj;
            return Arrays.equals(values, node.getValues()) && Objects.equals(statement, node.getStatement());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (values != null) {
            return "(" + this.getClass().getSimpleName() + " ("
                + Arrays.stream(values).map(String::valueOf).collect(Collectors.joining(" ")) + ") " + statement + ")";
        } else {
            return "(" + this.getClass().getSimpleName() + " default " + statement + ")";
        }
    }
}
