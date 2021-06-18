package daro.game.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import daro.game.main.Challenge;
import daro.game.validation.Validation;

import java.io.File;
import java.util.List;

/**
 * Utility class: A class that helps parsing challenges from jsonStrings.
 *
 * @author Daniel Planötscher
 */
public final class ChallengeParser {

    private ChallengeParser() {
        // Disallow instantiation
    }

    /**
     * Parses a {@link Challenge} from a JsonString
     *
     * @param jsonString the string you want to parse
     * @param source     the source file (needed for a Challenge)
     * @return the finished challenge object
     */
    public static Challenge parse(String jsonString, File source) {
        JsonObject obj = JsonParser.parseString(jsonString).getAsJsonObject();
        String name = obj.get("name").getAsString();
        String creator = obj.get("creator").getAsString();
        String description = obj.get("description").getAsString();
        JsonArray tests = obj.get("tests").getAsJsonArray();
        List<Validation> testsList = ValidationParser.parse(tests);
        String code;
        if (obj.get("currentCode") == null) {
            code = obj.get("startCode") == null ? "" : obj.get("startCode").getAsString();
        } else {
            code = obj.get("currentCode").getAsString();
        }
        boolean completed = obj.get("completion") != null && obj.get("completion").getAsBoolean();
        return new Challenge(name, description, code, testsList, creator, source, completed);
    }
}
