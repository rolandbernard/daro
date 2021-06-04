package daro.game.validation;

import daro.game.main.Level;
import daro.lang.interpreter.Interpreter;
import daro.lang.interpreter.InterpreterException;
import daro.lang.values.DaroArray;
import daro.lang.values.DaroObject;
import daro.lang.values.DaroTypeArray;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Validation {
    private long id;
    private ValidationType type;
    private String source;
    private DaroObject expected;

    /**
     * Generates a test for daro without an expected value.
     *
     * @param id     number of test for the level
     * @param type   a type of test
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
     *
     * @param id       number of test for the level
     * @param type     a type of test
     * @param source   variable / function call that has to be tested
     * @param expected the expected value. Either a simple value or an array in the
     *                 form of e.g. [2, 3, 4]
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
     * @param code        the code that has to be validated
     * @param validations a list containing tests that have to be run
     * @return a list of test results
     */
    public static List<ValidationResult> run(String code, List<Validation> validations) {
        return validations.stream().map(v -> v.runTest(code)).collect(Collectors.toList());
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
        String expectedString = null;
        try {
            DaroObject codeResult = interpreter.execute(code + source);
            givenResult = codeResult.toString();

            switch (type) {
                case EQUALS:
                    success = codeResult.equals(expected);
                    break;
                case NOT_EQUALS:
                    success = !codeResult.equals(expected);
                    expectedString = "not " + expected;
                    break;
                case TRUE:
                    success = codeResult.isTrue();
                    expectedString = "to be a truthy value";
                    break;
                case FALSE:
                    success = !codeResult.isTrue();
                    expectedString = "to be a falsy value";
                    break;
                case ARRAY_INCLUDES:
                    success = validateArrayIncludes(codeResult);
                    expectedString = "to contain " + expected;
                    break;
                case ARRAY_EXCLUDES:
                    success = validateArrayExcludes(codeResult);
                    expectedString = "to not contain " + expected;
                    break;

            }
        } catch (InterpreterException e) {
            givenResult = "There was an issue with your code: " + e.getMessage();
        }

        expectedString = expectedString == null ? expected.toString() : expectedString;
        return new ValidationResult(id, success, expectedString, givenResult);
    }

    /**
     * Checks if the codeResult is an array and contains a certain element or list
     * of elements
     *
     * @param codeResult the actual code result
     * @return if the expected value of the validation is contained in the code
     *         result
     */
    private boolean validateArrayIncludes(DaroObject codeResult) {
        try {
            DaroArray array = (DaroArray)codeResult;
            if (expected.getType().equals(new DaroTypeArray())) {
                DaroArray expectedArray = (DaroArray)expected;
                return expectedArray.getValues()
                    .stream()
                    .allMatch(a -> array.getValues().stream().anyMatch(e -> e.equals(a)));
            }
            return array.getValues().stream().anyMatch(i -> i.equals(expected));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the codeResult is an array and doesn't contain a certain element
     *
     * @param codeResult the actual code result
     * @return if the expected value of the validation is contained in the code
     *         result
     */
    private boolean validateArrayExcludes(DaroObject codeResult) {
        try {
            DaroArray array = (DaroArray)codeResult;
            if (expected.getType().equals(new DaroTypeArray())) {
                DaroArray expectedArray = (DaroArray)expected;
                return expectedArray.getValues()
                    .stream()
                    .noneMatch(a -> array.getValues().stream().anyMatch(e -> e.equals(a)));
            }
            return array.getValues().stream().noneMatch(i -> i.equals(expected));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parses the expected result from a user input and returns the UserObject. User
     * can input an array with e.g. [10, 20, 30]
     *
     * @param expected expected string value
     * @return UserObject to compare to actual value
     */
    private DaroObject parseExpectedResult(String expected) {
        DaroObject expectedResult;
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
            throw new IllegalArgumentException(
                "There was an issue with interpreting the expected values of the tests."
            );
        }
    }

    /**
     * Parses tests from a json
     * 
     * @param tests a JsonArray containing tests
     * @return a list of tests
     */
    public static List<Validation> parseFromJson(JSONArray tests) {
        List<Validation> testsList = new ArrayList<>();
        if (tests != null && tests.size() > 0) {
            tests.forEach(test -> {
                JSONObject testJson = (JSONObject)test;
                long id = (long)testJson.get("id");
                String source = testJson.get("source").toString();
                String expected = (String)testJson.get("expected");
                ValidationType type = ValidationType.valueOf((String)testJson.get("type"));

                Validation validation;
                if (expected.isEmpty()) {
                    validation = new Validation(id, type, source);
                } else {
                    validation = new Validation(id, type, source, expected);
                }
                testsList.add(validation);
            });
        }
        return testsList;
    }
}
