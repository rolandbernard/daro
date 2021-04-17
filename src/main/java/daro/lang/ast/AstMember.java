package daro.lang.ast;

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

