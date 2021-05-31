package daro.game.validation;

import daro.lang.interpreter.Interpreter;
import daro.lang.interpreter.InterpreterException;
import daro.lang.values.UserObject;

import java.util.ArrayList;
import java.util.List;

public class Validation {
    private long id;
    private ValidationType type;
    private String source;
    private UserObject expected;


    public Validation(long id, ValidationType type, String source) {
        if (type.needsExpectedValue())
            throw new IllegalArgumentException("ValidationType " + type + " needs an expected value.");
        this.id = id;
        this.type = type;
        this.source = source;
    }

    public Validation(long id, ValidationType type, String source, String expected) {
        if (!type.needsExpectedValue())
            throw new IllegalArgumentException("ValidationType " + type + " doesn't need an expected value.");
        this.id = id;
        this.type = type;
        this.source = source;
        this.expected = parseExpectedResult(expected);
    }


    public static List<ValidationResult> run(String code, List<Validation> validations) {
        List<ValidationResult> result = new ArrayList<>();
        for (Validation validation : validations) {
            result.add(validation.runTest(code));
        }
        return result;
    }

    private ValidationResult runTest(String code) {
        Interpreter interpreter = new Interpreter();
        boolean success = false;
        String givenResult;
        try {
            UserObject codeResult = interpreter.execute(code + source);
            switch (type) {
                case EQUALS:
                    success = codeResult.equals(expected);
                    break;
                case TRUE:
                    success = codeResult.isTrue();
                    break;
                case FALSE:
                    success = !codeResult.isTrue();
                    break;
            }
            givenResult = codeResult.toString();
        } catch (InterpreterException e) {
            givenResult = "There was an issue with your code: " + e.getMessage();
        }


        return new ValidationResult(id, success, expected.toString(), givenResult);
    }

    private UserObject parseExpectedResult(String expected) {
        UserObject expectedResult;
        Interpreter interpreter = new Interpreter();

        try {
            if (expected.matches("^\\[(.+,)*.+]$")) {
                String items = expected.substring(1, expected.length() - 1);
                expectedResult = interpreter.execute("a = new array; a.push(" + items + "); a");
            } else {
                expectedResult = interpreter.execute(expected);
            }
            return expectedResult;
        } catch (InterpreterException e) {
            throw new IllegalArgumentException("There was an issue with interpreting the expected values of the tests.");
        }
    }
}
