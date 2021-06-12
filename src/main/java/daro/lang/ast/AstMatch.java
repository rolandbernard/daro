package daro.lang.ast;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class representing an ast node for a match statement. e.g. {@code match x {
 * ... }}
 * 
 * @author Roland Bernard
 */
public final class AstMatch extends AstNode {
    private final AstNode value;
    private final AstMatchCase[] cases;

    public AstMatch(Position position, AstNode value, AstMatchCase[] cases) {
        super(position);
        this.value = value;
        this.cases = cases;
    }

    public AstNode getValue() {
        return value;
    }

    public AstMatchCase[] getCases() {
        return cases;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AstNode[] getChildren() {
        AstNode[] ret = Arrays.copyOf(cases, cases.length + 1, AstNode[].class);
        ret[cases.length] = value;
        return ret;
    }

    @Override
    public int hashCode() {
        return (997 * Objects.hashCode(value)) ^ (991 * Arrays.hashCode(cases));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstMatch) {
            AstMatch node = (AstMatch)obj;
            return Objects.equals(value, node.getValue()) && Arrays.equals(cases, node.getCases());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + value + " ("
            + Arrays.stream(cases).map(String::valueOf).collect(Collectors.joining(" ")) + "))";
    }
}
