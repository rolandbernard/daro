package daro.lang.ast;

/**
 * Class representing an ast node for a member access. e.g. {@code foo.bar}
 * 
 * @author Roland Bernard
 */
public class AstMember extends AstNode {
    private final AstNode operator;
    private final String name;

    public AstMember(Position position, AstNode operator, String name) {
        super(position);
        this.operator = operator;
        this.name = name;
    }

    public AstNode getOperator() {
        return operator;
    }

    public String getName() {
        return name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

