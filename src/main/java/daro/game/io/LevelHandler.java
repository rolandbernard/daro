package daro.game.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import daro.game.main.Level;
import daro.game.main.LevelGroup;
import daro.game.validation.Validation;
import daro.game.validation.ValidationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class LevelHandler {
    public static Level getNextLevel(long groupId, long levelId) {
        JsonArray groups = getLevelGroups();
        JsonObject group = findById(groups, groupId);
        if (group != null) {
            JsonArray levels = group.get("levels").getAsJsonArray();
            long nextGroupId = groupId;

            JsonObject nextLevelJSON = findById(levels, levelId + 1);
            if (nextLevelJSON == null) {
                nextGroupId = groupId + 1;
                JsonObject nextGroup = findById(groups, nextGroupId);
                if (nextGroup == null)
                    return null;
                JsonArray nextGroupLevels = nextGroup.get("levels").getAsJsonArray();
                if (nextGroupLevels == null || nextGroupLevels.isEmpty())
                    return null;
                nextLevelJSON = nextGroupLevels.get(0).getAsJsonObject();
            }

            if (nextLevelJSON == null)
                return null;
            Map<Long, JsonObject> completionMap = UserData.getLevelGroupData(nextGroupId);
            return parseLevelFromJsonObject(nextGroupId, nextLevelJSON, completionMap);
        }
        return null;
    }

    /**
     * Parses all the Levels and its groups from a JSON-File called levels.json
     *
     * @return A list of all the level groups
     */
    public static List<LevelGroup> getAllLevels() {
        List<LevelGroup> groupsList = new ArrayList<>();
        JsonArray groups = getLevelGroups();

        if (groups != null && groups.size() > 0) {
            groups.forEach(group -> groupsList.add(parseLevelGroupData(group.getAsJsonObject())));
        }
        return groupsList;
    }

    /**
     * Parses levels from a JsonArray
     *
     * @param parentId the id of the level group
     * @param levels   a JsonArray containing levels as JSON Objects
     * @return a list of levels
     */
    private static List<Level> parseLevelFromJson(long parentId, JsonArray levels) {
        List<Level> levelsList = new ArrayList<>();
        Map<Long, JsonObject> completionMap = UserData.getLevelGroupData(parentId);

        if (levels != null && levels.size() > 0) {
            levels.forEach(level -> {
                JsonObject levelJson = level.getAsJsonObject();

                levelsList.add(parseLevelFromJsonObject(parentId, levelJson, completionMap));
            });
        }
        return levelsList;
    }

    private static JsonObject findById(JsonArray list, long id) {
        for (JsonElement element : list) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.get("id").getAsLong() == id)
                return obj;
        }
        return null;
    }

    private static JsonArray getLevelGroups() {
        JsonElement jsonObject = PathHandler.getJsonData("levels.json");
        return jsonObject.getAsJsonObject().get("groups").getAsJsonArray();
    }

    private static LevelGroup parseLevelGroupData(JsonObject groupData) {
        long id = groupData.get("id").getAsLong();
        String name = groupData.get("name").getAsString();
        String description = groupData.get("description_short").getAsString();

        JsonArray levels = groupData.get("levels").getAsJsonArray();
        List<Level> levelsList = parseLevelFromJson(id, levels);

        return new LevelGroup(id, name, description, levelsList);
    }

    private static Level parseLevelFromJsonObject(
        long parentId, JsonObject levelJson, Map<Long, JsonObject> completionMap
    ) {
        long id = levelJson.get("id").getAsLong();
        String name = levelJson.get("name").getAsString();
        String description = levelJson.get("description").getAsString();
        JsonArray tests = levelJson.get("tests") != null ? levelJson.get("tests").getAsJsonArray() : null;
        List<Validation> testsList = parseValidationsFromJson(tests);
        String standardCode = levelJson.get("startCode") == null ? "" : levelJson.get("startCode").getAsString();
        boolean isCompleted = false;
        String currentCode = null;

        if (completionMap != null) {
            JsonObject data = completionMap.get(id);
            if (data != null) {
                isCompleted = data.get("completed").getAsBoolean();
                currentCode = data.get("currentCode").getAsString();
            }
        }

        String code = currentCode == null ? standardCode : currentCode;
        return new Level(id, name, description, isCompleted, code, testsList, parentId);
    }

    /**
     * Parses validations from a json
     *
     * @param validations a JsonArray containing validations
     * @return a list of tests
     */
    private static List<Validation> parseValidationsFromJson(JsonArray validations) {
        List<Validation> testsList = new ArrayList<>();
        if (validations != null && validations.size() > 0) {
            validations.forEach(test -> {
                JsonObject validation = test.getAsJsonObject();
                long id = validation.get("id").getAsLong();
                String source = validation.get("source").getAsString();
                String expected = validation.get("expected").getAsString();
                ValidationType type = ValidationType.valueOf(validation.get("type").getAsString());

                Validation validationItem;
                if (expected.isEmpty()) {
                    validationItem = new Validation(id, type, source);
                } else {
                    validationItem = new Validation(id, type, source, expected);
                }
                testsList.add(validationItem);
            });
        }
        return testsList;
    }

}
