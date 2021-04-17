package daro.lang.ast;

public class Position {
    private final int start;
    private final int end;

    public Position(int start, int length) {
        this.start = start;
        this.end = start + length;
    }

    int getStart() {
        return start;
    }

    int getEnd() {
        return end;
    }

    int getLength() {
        return end - start;
    }
}
