package daro.game.parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import daro.game.validation.Validation;
import daro.game.validation.ValidationType;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ValidationParserTest {
    @Test
    void shouldParseValidation() {
        String string = "{" +
                "              \"id\": 1," +
                "              \"source\": \"a\"," +
                "              \"expected\": \"10\"," +
                "              \"type\": \"EQUALS\"" +
                "            }";
        JsonObject parsedObject = JsonParser.parseString(string).getAsJsonObject();
        Validation parsedValidation = ValidationParser.parse(parsedObject);
        Validation expectedValidation = new Validation(1, ValidationType.EQUALS, "a", "10");
        assertEquals(expectedValidation, parsedValidation);
    }

    @Test
    void shouldParseArrayValidation() {
        String string = "{" +
                "              \"id\": 1," +
                "              \"source\": \"arr\"," +
                "              \"expected\": \"10\"," +
                "              \"type\": \"ARRAY_INCLUDES\"" +
                "            }";
        JsonObject parsedObject = JsonParser.parseString(string).getAsJsonObject();
        Validation parsedValidation = ValidationParser.parse(parsedObject);
        Validation expectedValidation = new Validation(1, ValidationType.ARRAY_INCLUDES, "arr", "10");
        assertEquals(expectedValidation, parsedValidation);
    }

    @Test
    void shouldFailParsingValidation() {
        String string = "{" +
                "              \"id\": 1," +
                "              \"source\": \"a\"," +
                "              \"expected\": \"10\"," +
                "              \"type\": \"TRUE\"" +
                "            }";
        JsonObject parsedObject = JsonParser.parseString(string).getAsJsonObject();
        assertThrows(IllegalArgumentException.class, () -> ValidationParser.parse(parsedObject));
    }

    @Test
    void shouldFailParsingValidation2() {
        String string = "{" +
                "              \"id\": 1," +
                "              \"source\": \"a\"," +
                "              \"expected\": \"10\"," +
                "              \"type\": \"FALSE\"" +
                "            }";
        JsonObject parsedObject = JsonParser.parseString(string).getAsJsonObject();
        assertThrows(IllegalArgumentException.class, () -> ValidationParser.parse(parsedObject));

    }
}
