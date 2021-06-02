package daro.game.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class UserData {
    private static final String USER_PATH = "./user/";

    private static JSONObject parseUserDataJson() {
        JSONObject obj = new JSONObject();
        JSONParser parser = new JSONParser();
        File file = new File(USER_PATH + "data.json");
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\\Z");
            String jsonString = "";
            if (scanner.hasNext()) {
                jsonString = scanner.next();
            }

            scanner.close();
            try {
                obj = (JSONObject) parser.parse(jsonString);

            } catch (ParseException e) {
                return obj;
            }
        } catch (FileNotFoundException ex) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static Map<Long, Map<String, String>> getLevelGroupData(long groupId) {
        JSONObject object = parseUserDataJson();
        Map<Long, Map<String, String>> map = new HashMap<>();
        Object levelGroupData = object.get(String.valueOf(groupId));
        if (levelGroupData != null) {
            JSONArray groupData = (JSONArray) levelGroupData;
            for (Object level : groupData) {
                JSONObject levelData = (JSONObject) level;
                Map<String, String> levelDataMap = new HashMap<>();
                levelDataMap.put("completed", (boolean) levelData.get("completed") ? "true" : "");
                levelDataMap.put("currentCode", (String) levelData.get("currentCode"));
                map.put((long) levelData.get("id"), levelDataMap);
            }
        }
        return map;
    }

    public static boolean writeLevelData(long groupId, long levelId, boolean completion, String currentCode) {
        JSONObject object = parseUserDataJson();
        Object levelGroupData = object.get(String.valueOf(groupId));
        if (levelGroupData == null) {
            JSONArray levels = new JSONArray();
            JSONObject level = new JSONObject();
            level.put("id", levelId);
            level.put("completed", completion);
            level.put("currentCode", currentCode);
            levels.add(level);
            object.put(groupId, levels);
        } else {
            JSONArray levelGroup = (JSONArray) levelGroupData;
            for (Object o : levelGroup) {
                JSONObject level = (JSONObject) o;
                if ((long) level.get("id") == levelId) {
                    level.replace("completed", completion);
                    level.replace("currentCode", currentCode);
                    break;
                }
            }
            object.replace(groupId, levelGroup);
        }
        try (PrintWriter file = new PrintWriter(USER_PATH + "data.json")) {
            file.write(object.toJSONString());
            file.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
