package daro.lang.ast;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class representing an ast node for a function definition. e.g. {@code fn foo(
 * ... ): bar { ... }}
 * 
 * @author Roland Bernard
 */
public final class AstFunction extends AstNode {
    private final String name;
    private final AstSymbol[] parameters;
    private final AstBlock body;

    public AstFunction(Position position, String name, AstSymbol[] parameters, AstBlock body) {
        super(position);
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public AstSymbol[] getParameters() {
        return parameters;
    }

    public AstBlock getBody() {
        return body;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public AstNode[] getChildren() {
        AstNode[] ret = Arrays.copyOf(parameters, parameters.length + 1, AstNode[].class);
        ret[parameters.length] = body;
        return ret;
    }

    @Override
    public int hashCode() {
        return (997 * name.hashCode()) ^ (877 * Arrays.hashCode(parameters)) ^ (953 * body.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstFunction) {
            AstFunction node = (AstFunction)obj;
            return Objects.equals(name, node.getName()) && Arrays.equals(parameters, node.getParameters())
                && Objects.equals(body, node.getBody());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName() + " " + name + " ("
            + Arrays.stream(parameters).map(String::valueOf).collect(Collectors.joining(" ")) + ") " + body + ")";
    }
}
