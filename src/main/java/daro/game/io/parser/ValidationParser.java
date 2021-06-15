package daro.game.io.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import daro.game.validation.Validation;
import daro.game.validation.ValidationType;
import org.reactfx.value.Val;

import java.util.ArrayList;
import java.util.List;

public final class ValidationParser {
    private ValidationParser() {

    }

    /**
     * Parses a list of validations from a json
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
