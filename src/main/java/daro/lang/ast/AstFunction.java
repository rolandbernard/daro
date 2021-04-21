package daro.lang.ast;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class representing an ast node for a function definition. e.g. {@code fn foo( ... ): bar { ... }}
 * 
 * @author Roland Bernard
 */
public final class AstFunction extends AstNode {
    private final String name;
    private final AstNode type;
    private final AstDefinition[] parameters;
    private final AstBlock body;

    public AstFunction(Position position, String name, AstNode type, AstDefinition[] parameters, AstBlock body) {
        super(position);
        this.name = name;
        this.type = type;
        this.parameters = parameters;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public AstNode getType() {
        return type;
    }

    public AstDefinition[] getParameters() {
        return parameters;
    }

    public AstBlock getBody() {
        return body;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return (997 * name.hashCode())
            ^ (991 * type.hashCode())
            ^ (877 * Arrays.hashCode(parameters))
            ^ (953 * body.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstFunction) {
            AstFunction node = (AstFunction)obj;
            return Objects.equals(name, node.getName())
                && Objects.equals(type, node.getType())
                && Arrays.equals(parameters, node.getParameters())
                && Objects.equals(body, node.getBody());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "("
            + this.getClass().getSimpleName() + " " + name + " " + type + " ("
            + Arrays.stream(parameters).map(String::valueOf).collect(Collectors.joining(" "))
            + ") " + body + ")";
    }
}

