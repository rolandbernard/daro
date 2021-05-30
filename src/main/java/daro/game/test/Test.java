package daro.game.test;

import daro.lang.interpreter.Interpreter;
import daro.lang.interpreter.InterpreterException;
import daro.lang.values.UserObject;

import java.util.ArrayList;
import java.util.List;

public class Test {
    private long id;
    private TestType type;
    private TestSource source;
    private String sourceName, expected;

    public Test(long id, TestType type, TestSource source, String sourceName, String expectedString) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.sourceName = sourceName;
        this.expected = expectedString;
    }

    public static List<TestResult> run(String code, List<Test> tests) {
        List<TestResult> result = new ArrayList<>();
        for(Test test: tests) {
            result.add(test.runTest(code));
        }
        System.out.println(result);
        return result;
    }

    private TestResult runTest(String code) {
        Interpreter interpreter = new Interpreter();
        Interpreter expectedInterpreter = new Interpreter();
        boolean success = false;
        String givenResult;
        try {
            UserObject userCodeResult = interpreter.execute(code + sourceName + (source == TestSource.FUNCTION ? "()" : ""));
            UserObject expectedResult = expectedInterpreter.execute(expected);

            switch(type) {
                case EQUALS:
                    success = userCodeResult.equals(expectedResult);
                    break;
                case TRUE:
                    success = userCodeResult.isTrue();
                    break;
                case FALSE:
                    success = !userCodeResult.isTrue();
                    break;
            }
            givenResult = userCodeResult.toString();
        } catch (InterpreterException e) {
            givenResult = "There was an issue with your code: " + e.getMessage();
        }


        return new TestResult(id, success, expected, givenResult);
    }
}
