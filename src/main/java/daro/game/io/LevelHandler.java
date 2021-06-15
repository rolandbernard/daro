package daro.game.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import daro.game.io.parser.CompletionParser;
import daro.game.io.parser.LevelParser;
import daro.game.io.parser.ValidationParser;
import daro.game.main.Level;
import daro.game.main.LevelGroup;
import daro.game.validation.Validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class LevelHandler {

    private LevelHandler() {
    }

    public static Level getNextLevel(long groupId, long levelId) {
        String levelJson = ResourceHandler.getDataContent("levels.json");
        if(levelJson != null) {
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
     * Parses all the Levels and its groups from a JSON-File called levels.json
     *
     * @return A list of all the level groups
     */
    public static List<LevelGroup> getAllLevels() {
        String levelGroups = ResourceHandler.getDataContent("levels.json");
        return LevelParser.parseGroups(levelGroups, true);
    }

    private static JsonObject findById(JsonArray list, long id) {
        for (JsonElement element : list) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.get("id").getAsLong() == id)
                return obj;
        }
        return null;
    }
}
