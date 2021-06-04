package daro.lang.ast;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class representing an ast node for calls. e.g. {@code foo(1, 2, 3)}
 * 
 * @author Roland Bernard
 */
public final class AstCall extends AstNode {
    private final AstNode function;
    private final AstNode[] parameters;

    public AstCall(Position position, AstNode function, AstNode[] parameters) {
        super(position);
        this.function = function;
        this.parameters = parameters;
    }

    public AstNode getFunction() {
        return function;
    }

    public AstNode[] getParameters() {
        return parameters;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return (997 * function.hashCode()) ^ (991 * Arrays.hashCode(parameters));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstCall) {
            AstCall node = (AstCall)obj;
            return Objects.equals(function, node.getFunction()) && Arrays.equals(parameters, node.getParameters());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + function + " ("
            + Arrays.stream(parameters).map(String::valueOf).collect(Collectors.joining(" ")) + "))";
    }
}
