package daro.game.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import daro.game.io.UserData;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class:
 * A class that helps parsing the completion from UserData.
 *
 * @author Daniel Planötscher
 */
public final class CompletionParser {

    private CompletionParser() {
        // Disallow instantiation
    }

    /**
     * Parses all the completions from each LevelGroup
     *
     * @return a map containing all the completions for each LevelGroup
     */
    public static Map<Long, Map<Long, JsonObject>> parse() {
        JsonObject object = UserData.parseUserJson("data.json");
        Map<Long, Map<Long, JsonObject>> map = new HashMap<>();
        for (String key : object.keySet()) {
            JsonArray groups = object.get(key).getAsJsonArray();
            Map<Long, JsonObject> subMap = new HashMap<>();
            for (JsonElement level : groups) {
                JsonObject obj = level.getAsJsonObject();
                subMap.put(obj.get("id").getAsLong(), obj);
            }
            map.put(Long.parseLong(key), subMap);
        }
        return map;
    }

    /**
     * Parses all the completions from a specific LevelGroup Id
     *
     * @param groupId the LevelGroupId you want to parse it from
     * @return a Map containing the completions
     */
    public static Map<Long, JsonObject> parse(long groupId) {
        JsonObject object = UserData.parseUserJson("data.json");
        JsonElement groupData = object.get(String.valueOf(groupId));
        return parse(groupData);
    }

    /**
     * Helper method that does the parsing of the data for both methods above
     *
     * @param groupData the JsonElement of the group you want to parse
     * @return the completion map
     */
    private static Map<Long, JsonObject> parse(JsonElement groupData) {
        Map<Long, JsonObject> map = new HashMap<>();
        if (groupData != null) {
            JsonArray group = groupData.getAsJsonArray();
            for (JsonElement level : group) {
                JsonObject obj = level.getAsJsonObject();
                map.put(obj.get("id").getAsLong(), obj);
            }
        }
        return map;
    }
}
