package daro.game.parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import daro.game.main.Level;
import daro.game.main.LevelGroup;
import daro.game.validation.Validation;
import daro.game.validation.ValidationType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LevelParserTest {

    @Test
    void shouldParseLevel() {
        String levelString = "{" +
                "          \"id\": 1," +
                "          \"name\": \"Create a variable\"," +
                "          \"description\": \"Create a variable with the name a and assign it to the value 10\"," +
                "          \"startCode\": \"// code goes here\\n// or here\"," +
                "          \"tests\": [" +
                "            {" +
                "              \"id\": 1," +
                "              \"source\": \"a\"," +
                "              \"expected\": \"10\"," +
                "              \"type\": \"EQUALS\"" +
                "            }" +
                "          ]" +
                "        }";
        JsonObject parsedObject = JsonParser.parseString(levelString).getAsJsonObject();
        Level parsedLevel = LevelParser.parseLevel(1, parsedObject, new HashMap<>());
        Level expectedLevel = new Level(1, "Create a variable",
                "Create a variable with the name a and assign it to the value 10",
                false, "// code goes here\\n// or here",
                List.of(
                        new Validation(1, ValidationType.EQUALS, "a", "10")
                ), 1, null, null);
        assertEquals(parsedLevel, expectedLevel);

    }

    @Test
    void shouldParseLevelWithHelp() {
        String levelString = "{" +
                "          \"id\": 1," +
                "          \"name\": \"Create a variable\"," +
                "          \"description\": \"Create a variable with the name a and assign it to the value 10\"," +
                "          \"startCode\": \"// code goes here\\n// or here\"," +
                "\"help\": {\"text\": \"HelpText\", \"code\":\"HelpCode\"}," +
                "          \"tests\": [" +
                "            {" +
                "              \"id\": 1," +
                "              \"source\": \"a\"," +
                "              \"expected\": \"10\"," +
                "              \"type\": \"EQUALS\"" +
                "            }" +
                "          ]" +
                "        }";
        JsonObject parsedObject = JsonParser.parseString(levelString).getAsJsonObject();
        Level parsedLevel = LevelParser.parseLevel(1, parsedObject, new HashMap<>());
        Level expectedLevel = new Level(1, "Create a variable",
                "Create a variable with the name a and assign it to the value 10",
                false, "// code goes here\\n// or here",
                List.of(
                        new Validation(1, ValidationType.EQUALS, "a", "10")
                ), 1, "HelpText", "HelpCode");
        assertEquals(parsedLevel, expectedLevel);

    }

    @Test
    void shouldParseSimpleGroup() {
        String groupString = "{\"groups\": [" +
                "    {" +
                "      \"id\": 1," +
                "      \"name\": \"Data\"," +
                "      \"description\": \"Variables and basic data structures\"," +
                "      \"levels\": [" +
                "        {" +
                "          \"id\": 1," +
                "          \"name\": \"Create a variable\"," +
                "          \"description\": \"Create a variable with the name a and assign it to the value 10\"," +
                "          \"startCode\": \"// code goes here\\n// or here\"," +
                "          \"tests\": [" +
                "            {" +
                "              \"id\": 1," +
                "              \"source\": \"a\"," +
                "              \"expected\": \"10\"," +
                "              \"type\": \"EQUALS\"" +
                "            }" +
                "          ]" +
                "        }" +
                "      ]" +
                "    }" +
                "  ]" +
                "}";
        List<LevelGroup> parsedGroups = LevelParser.parseGroups(groupString, false);
        List<LevelGroup> expectedGroups = List.of(
                new LevelGroup(1, "Data", "Variables and basic data structures",
                        List.of(
                                new Level(1, "Create a variable",
                                        "Create a variable with the name a and assign it to the value 10",
                                        false, "// code goes here\\n// or here",
                                        List.of(
                                                new Validation(1, ValidationType.EQUALS, "a", "10")
                                        ), 1, null, null
                                ))));
        assertEquals(parsedGroups, expectedGroups);
    }

    @Test
    void shouldParseComplexGroup() {
        String groupString = "{\"groups\": [" +
                "    {" +
                "      \"id\": 1," +
                "      \"name\": \"Data\"," +
                "      \"description\": \"Variables and basic data structures\"," +
                "      \"levels\": [" +
                "        {" +
                "          \"id\": 1," +
                "          \"name\": \"Create a variable\"," +
                "          \"description\": \"Create a variable with the name a and assign it to the value 10\"," +
                "          \"startCode\": \"// code goes here\\n// or here\"," +
                "          \"tests\": [" +
                "            {" +
                "              \"id\": 1," +
                "              \"source\": \"a\"," +
                "              \"expected\": \"10\"," +
                "              \"type\": \"EQUALS\"" +
                "            }" +
                "          ]" +
                "        }," +
                "        {" +
                "          \"id\": 2," +
                "          \"name\": \"Create a variable\"," +
                "          \"description\": \"Create a variable with the name a and assign it to the value 10\"," +
                "          \"startCode\": \"// code goes here\\n// or here\"," +
                "          \"tests\": [" +
                "            {" +
                "              \"id\": 1," +
                "              \"source\": \"a\"," +
                "              \"expected\": \"10\"," +
                "              \"type\": \"EQUALS\"" +
                "            }," +
                "            {" +
                "              \"id\": 2," +
                "              \"source\": \"b\"," +
                "              \"expected\": \"10\"," +
                "              \"type\": \"NOT_EQUALS\"" +
                "            }" +
                "          ]" +
                "        }" +
                "      ]" +
                "    }" +
                "  ]" +
                "}";
        List<LevelGroup> parsedGroups = LevelParser.parseGroups(groupString, false);
        List<LevelGroup> expectedGroups = List.of(
                new LevelGroup(1, "Data", "Variables and basic data structures",
                        List.of(
                                new Level(1, "Create a variable",
                                        "Create a variable with the name a and assign it to the value 10",
                                        false, "// code goes here\\n// or here",
                                        List.of(
                                                new Validation(1, ValidationType.EQUALS, "a", "10")
                                        ), 1, null, null
                                ),
                                new Level(2, "Create a variable",
                                        "Create a variable with the name a and assign it to the value 10",
                                        false, "// code goes here\\n// or here",
                                        List.of(
                                                new Validation(1, ValidationType.EQUALS, "a", "10"),
                                                new Validation(2, ValidationType.NOT_EQUALS, "b", "10")
                                        ), 1, null, null
                                ))));
        assertEquals(parsedGroups, expectedGroups);
    }
}
