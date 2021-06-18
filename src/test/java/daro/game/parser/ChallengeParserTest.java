package daro.game.parser;

import daro.game.main.Challenge;
import daro.game.validation.Validation;
import daro.game.validation.ValidationType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChallengeParserTest {
    @Test
    void shouldParseDefaultChallenge() {
        String string = "{" +
                "          \"id\": 1," +
                "          \"name\": \"Create a variable\"," +
                "          \"description\": \"Create a variable with the name a and assign it to the value 10\"," +
                "          \"defaultCode\": \"// code goes here\\n// or here\"," +
                "          \"creator\": \"User\"," +
                "          \"tests\": [" +
                "            {" +
                "              \"id\": 1," +
                "              \"source\": \"a\"," +
                "              \"expected\": \"10\"," +
                "              \"type\": \"EQUALS\"" +
                "            }" +
                "          ]" +
                "        }";

        Challenge parsed = ChallengeParser.parse(string, null);
        Challenge expected = new Challenge("Create a variable",
                "Create a variable with the name a and assign it to the value 10",
                "// code goes here\\n// or here",
                List.of(
                        new Validation(1, ValidationType.EQUALS, "a", "10")
                ), "User", null, false);
        assertEquals(parsed, expected);
    }

    @Test
    void shouldParseEditedChallenge() {
        String string = "{" +
                "          \"id\": 1," +
                "          \"name\": \"Create a variable\"," +
                "          \"description\": \"Create a variable with the name a and assign it to the value 10\"," +
                "          \"defaultCode\": \"// code goes here\\n// or here\"," +
                "\"currentCode\": \"current code\"," +
                "\"completed\": true," +
                "          \"creator\": \"User\"," +
                "          \"tests\": [" +
                "            {" +
                "              \"id\": 1," +
                "              \"source\": \"a\"," +
                "              \"expected\": \"10\"," +
                "              \"type\": \"EQUALS\"" +
                "            }" +
                "          ]" +
                "        }";

        Challenge parsed = ChallengeParser.parse(string, null);
        Challenge expected = new Challenge("Create a variable",
                "Create a variable with the name a and assign it to the value 10",
                "current code",
                List.of(
                        new Validation(1, ValidationType.EQUALS, "a", "10")
                ), "User", null, true);
        assertEquals(parsed, expected);
    }
}
