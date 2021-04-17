package daro.lang.ast;

public abstract class AstNode {
    private final Position position;

    public AstNode(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public int getStart() {
        return position.getStart();
    }

    public int getEnd() {
        return position.getEnd();
    }

    public int getLength() {
        return position.getLength();
    }

    abstract public void accept(Visitor visitor);
}

