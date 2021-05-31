package daro.game.validation;

import daro.lang.interpreter.Interpreter;
import daro.lang.interpreter.InterpreterException;
import daro.lang.values.UserObject;

import java.util.List;
import java.util.stream.Collectors;

public class Validation {
    private long id;
    private ValidationType type;
    private String source;
    private UserObject expected;


    /**
     * Generates a test for daro without an expected value.
     * @param id number of test for the level
     * @param type a type of test
     * @param source variable / function call that has to be tested
     */
    public Validation(long id, ValidationType type, String source) {
        if (type.needsExpectedValue())
            throw new IllegalArgumentException("ValidationType " + type + " needs an expected value.");
        this.id = id;
        this.type = type;
        this.source = source;
    }

    /**
     * Generates a test for daro with an expected value.
     * @param id number of test for the level
     * @param type a type of test
     * @param source variable / function call that has to be tested
     * @param expected the expected value. Either a simple value or an array in the form of e.g. [2, 3, 4]
     */
    public Validation(long id, ValidationType type, String source, String expected) {
        if (!type.needsExpectedValue())
            throw new IllegalArgumentException("ValidationType " + type + " doesn't need an expected value.");
        this.id = id;
        this.type = type;
        this.source = source;
        this.expected = parseExpectedResult(expected);
    }


    /**
     * Runs all the tests of a list on a specific code
     *
     * @param code the code that has to be validated
     * @param validations a list containing tests that have to be run
     * @return a list of test results
     */
    public static List<ValidationResult> run(String code, List<Validation> validations) {
        return validations.stream()
                .map(v -> v.runTest(code))
                .collect(Collectors.toList());
    }

    /**
     * Runs a test on a code.
     *
     * @param code the Code that has to be tested
     * @return a ValidationResult Object, containing information about the test
     */
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

    /**
     * Parses the expected result from a user input and returns the UserObject.
     * User can input an array with e.g. [10, 20, 30]
     *
     * @param expected expected string value
     * @return UserObject to compare to actual value
     */
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
