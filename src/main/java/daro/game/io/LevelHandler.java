package daro.game.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import daro.game.parser.CompletionParser;
import daro.game.parser.LevelParser;
import daro.game.main.Level;
import daro.game.main.LevelGroup;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Utility class:
 * Helps handling Levels and reading them.
 *
 * @author Daniel Planötscher
 */
public final class LevelHandler {

    private LevelHandler() {
        // Disallow instantiation
    }

    /**
     * A method that searches for the next level from a starting point
     *
     * @param level the starting point to search from
     * @return the next level
     */
    public static Level getNextLevel(Level level) {
        long groupId = level.getGroupId();
        long levelId = level.getId();
        String levelJson = ResourceHandler.getDataContent("levels.json");
        if (levelJson != null) {
            JsonArray groups = JsonParser.parseString(levelJson).getAsJsonObject().get("groups").getAsJsonArray();
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
                Map<Long, JsonObject> completionMap = CompletionParser.parse(nextGroupId);
                return LevelParser.parseLevel(nextGroupId, nextLevelJSON, completionMap);
            }
        }
        return null;
    }

    /**
     * Parses all the Levels and its groups from a data JSON-File called levels.json
     *
     * @return A list of all the level groups
     */
    public static List<LevelGroup> getAllLevels() {
        String levelGroups = ResourceHandler.getDataContent("levels.json");
        return LevelParser.parseGroups(levelGroups, true);
    }

    /**
     * Finds an element by its ID attribute within a {@link JsonArray}
     *
     * @param list the JsonArray you want to search in
     * @param id   the id of the target
     * @return the JsonObject you searched for
     */
    private static JsonObject findById(JsonArray list, long id) {
        for (JsonElement element : list) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.get("id").getAsLong() == id)
                return obj;
        }
        return null;
    }

    /**
     * A method that rewrites the user data file by either creating or replacing an
     * old entry of user data concerning the completion of a level.
     *
     * @param groupId     the group id of the level
     * @param levelId     the level id
     * @param completion  the completion (if it was solved successfully)
     * @param currentCode the code written by the user
     * @return if the writing wsa successful or not.
     */
    public static boolean writeLevelData(long groupId, long levelId, boolean completion, String currentCode) {
        JsonObject outputObject = UserData.parseUserJson("data.json");
        JsonElement levelCompObj = outputObject.get(String.valueOf(groupId));
        if (levelCompObj == null) {
            JsonArray levels = new JsonArray();
            levels.add(createLevelDataEntry(levelId, completion, currentCode));
            outputObject.add(String.valueOf(groupId), levels);
        } else {
            JsonArray levelCompletions = levelCompObj.getAsJsonArray();
            for (int i = 0; i < levelCompletions.size(); i++) {
                JsonObject level = levelCompletions.get(i).getAsJsonObject();
                if (level.get("id").getAsLong() == levelId) {
                    levelCompletions.remove(i);
                    break;
                }
            }
            levelCompletions.add(createLevelDataEntry(levelId, completion, currentCode));
        }

        try {
            File dataFile = new File(UserData.USER_PATH + "data.json");
            IOHelpers.overwriteFile(dataFile, outputObject.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Creates a new JsonObject for a new completion entry
     *
     * @param levelId id of the level
     * @param completion the completion status
     * @param currentCode the written code
     * @return JsonObject to be used as completion
     */
    private static JsonObject createLevelDataEntry(long levelId, boolean completion, String currentCode) {
        JsonObject level = new JsonObject();
        level.addProperty("id", levelId);
        level.addProperty("completed", completion);
        level.addProperty("currentCode", currentCode);
        return level;
    }

}
