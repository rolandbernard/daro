package daro.game.io.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import daro.game.io.UserData;

import java.util.HashMap;
import java.util.Map;

public final class CompletionParser {
    private CompletionParser() {

    }

    public static Map<Long, Map<Long, JsonObject>> parseAll() {
        JsonObject object = UserData.parseUserJson("data.json");
        Map<Long, Map<Long, JsonObject>> map = new HashMap<>();
        for(String key : object.keySet()) {
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

    public static Map<Long, JsonObject> parse(long groupId) {
        JsonObject object = UserData.parseUserJson("data.json");
        JsonElement groupData = object.get(String.valueOf(groupId));
        return parse(groupData);
    }
    private static Map<Long, JsonObject> parse(JsonElement groupData) {
        Map<Long, JsonObject> map = new HashMap<>();
        if (groupData != null) {
            JsonArray groups = groupData.getAsJsonArray();
            for (JsonElement level : groups) {
                JsonObject obj = level.getAsJsonObject();
                map.put(obj.get("id").getAsLong(), obj);
            }
        }
        return map;

    }
}
