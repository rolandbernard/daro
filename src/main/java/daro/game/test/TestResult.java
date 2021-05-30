package daro.game.test;

public class TestResult {
    private boolean success;
    private String expected, actual;
    private final long id;

    public TestResult(long id, boolean success, String expected, String actual) {
        this.id = id;
        this.success = success;
        this.expected = expected;
        this.actual = actual;
    }

    public boolean evaluate() {
        return success;
    }

    @Override
    public String toString() {
        return "Test " + id + ":\n" + (success ? "Passed!" : "Expected: " + expected + "\n" + "Actual: " + actual);
    }
}
