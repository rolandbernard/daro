package daro.lang.ast;

/**
 * Class representing an ast node for calls. e.g. {@code foo(1, 2, 3)}
 * 
 * @author Roland Bernard
 */
public class AstCall extends AstNode {
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}

