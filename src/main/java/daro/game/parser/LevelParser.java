package daro.game.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import daro.game.main.Level;
import daro.game.main.LevelGroup;
import daro.game.validation.Validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class: A class that helps parsing the levels and its groups.
 *
 * @author Daniel Planötscher
 */
public final class LevelParser {

    /**
     * Parses a JsonString for a list of groups
     *
     * @param jsonString     the JsonString you want to parse
     * @param withCompletion if you want to parse it using the completions (false
     *                       only for debug)
     * @return a list of levelgroups
     */
    public static List<LevelGroup> parseGroups(String jsonString, boolean withCompletion) {
        JsonObject obj = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonArray groups = obj.get("groups").getAsJsonArray();
        List<LevelGroup> groupList = new ArrayList<>();
        Map<Long, Map<Long, JsonObject>> completionMap;
        if (withCompletion) {
            completionMap = CompletionParser.parse();
        } else {
            completionMap = new HashMap<>();
        }
        if (groups != null && groups.size() > 0) {
            groups.forEach(group -> groupList.add(parseGroup(group.getAsJsonObject(), completionMap)));
        }
        return groupList;
    }

    /**
     * Parses one specific levelgroup from a JsonObject
     *
     * @param groupObj    the jsonobject you want to parse
     * @param completions the completion map
     * @return a new LevelGroup
     */
    public static LevelGroup parseGroup(JsonObject groupObj, Map<Long, Map<Long, JsonObject>> completions) {
        long id = groupObj.get("id").getAsLong();
        String name = groupObj.get("name").getAsString();
        String description = groupObj.get("description").getAsString();

        JsonArray levels = groupObj.get("levels").getAsJsonArray();
        List<Level> levelsList = parseLevels(id, levels, completions.get(id));

        return new LevelGroup(id, name, description, levelsList);
    }

    /**
     * Parses a list of levels from a JsonArray
     *
     * @param parentId   the groupId it should be in
     * @param levels     the JsonArray of levels you want to parse
     * @param completion the completion map of the user
     * @return a list of levels
     */
    public static List<Level> parseLevels(long parentId, JsonArray levels, Map<Long, JsonObject> completion) {
        List<Level> levelsList = new ArrayList<>();
        if (levels != null && levels.size() > 0) {
            levels.forEach(level -> {
                JsonObject levelJson = level.getAsJsonObject();
                levelsList.add(parseLevel(parentId, levelJson, completion));
            });
        }
        return levelsList;
    }

    /**
     * Parses one specific level from a JsonObject, while keeping track of the
     * current data for the user
     *
     * @param parentId   the group id it should be in
     * @param levelJson  the JsonObject ot parse
     * @param completion the completion map of the user
     * @return a new Level
     */
    public static Level parseLevel(long parentId, JsonObject levelJson, Map<Long, JsonObject> completion) {
        long id = levelJson.get("id").getAsLong();
        boolean isCompleted = false;
        String currentCode = null;
        String name = levelJson.get("name").getAsString();
        String description = levelJson.get("description").getAsString();

        JsonArray tests = levelJson.get("tests") != null ? levelJson.get("tests").getAsJsonArray() : null;
        List<Validation> testsList = ValidationParser.parse(tests);

        String standardCode = levelJson.get("startCode") == null ? "" : levelJson.get("startCode").getAsString();

        if (completion != null) {
            JsonObject data = completion.get(id);
            if (data != null) {
                isCompleted = data.get("completed").getAsBoolean();
                currentCode = data.get("currentCode").getAsString();
            }
        }

        String helpText = null;
        String helpCode = null;

        if (levelJson.get("help") != null) {
            JsonObject help = levelJson.get("help").getAsJsonObject();
            helpText = help.get("text") != null ? help.get("text").getAsString() : null;
            helpCode = help.get("code") != null ? help.get("code").getAsString() : null;
        }

        String code = currentCode == null ? standardCode : currentCode;
        return new Level(id, name, description, isCompleted, code, testsList, parentId, helpText, helpCode);
    }
}
