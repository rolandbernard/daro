package daro.game.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import daro.game.validation.Validation;
import daro.game.validation.ValidationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class:
 * A class that helps parsing the validations.
 *
 * @author Daniel Planötscher
 */
public final class ValidationParser {

    private ValidationParser() {
        // Disallow instantiation
    }

    /**
     * Parses a list of validations from a JsonArray
     *
     * @param validations a JsonArray containing validations
     * @return a list of validations
     */
    public static List<Validation> parse(JsonArray validations) {
        List<Validation> testsList = new ArrayList<>();
        if (validations != null && validations.size() > 0) {
            validations.forEach(test -> {
                JsonObject validation = test.getAsJsonObject();
                testsList.add(parse(validation));
            });
        }
        return testsList;
    }

    /**
     * Parses a only a specific validation from a JsonObject
     *
     * @param validation a JsonObject containing validation information
     * @return a new Validation Object
     */
    public static Validation parse(JsonObject validation) {
        long id = validation.get("id").getAsLong();
        String source = validation.get("source").getAsString();
        String expected = validation.get("expected") != null ? validation.get("expected").getAsString() : null;
        ValidationType type = ValidationType.valueOf(validation.get("type").getAsString());

        Validation validationItem;
        if (expected == null) {
            validationItem = new Validation(id, type, source);
        } else {
            validationItem = new Validation(id, type, source, expected);
        }
        return validationItem;
    }
}
