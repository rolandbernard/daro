package daro.game.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import daro.game.ui.InputField;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public abstract class SettingsHandler {

    private static String SETTINGS_PATH = UserData.USER_PATH + "settings.json";

    private static JsonObject getSettings() {
        return UserData.parseUserJson("settings.json");
    }

    public static Map<String, Map<String, String>> getAllSettings() {
        JsonObject settings = getSettings();
        Map<String, Map<String, String>> settingsMap = new HashMap<>();
        if (settings != null) {
            for (String key : settings.keySet()) {
                JsonObject elements = settings.get(key).getAsJsonObject();
                Map<String, String> keyValue = generateMapFromJson(elements);
                settingsMap.put(key, keyValue);
            }
        }
        return settingsMap;
    }

    public static Map<String, String> getSettingsByKey(String key) {
        JsonObject settings = getSettings();
        Map<String, String> settingsMap = new HashMap<>();
        if (settings != null) {
            JsonObject elements = settings.get(key).getAsJsonObject();
            settingsMap = generateMapFromJson(elements);
        }
        return settingsMap;
    }

    private static Map<String, String> generateMapFromJson(JsonObject elements) {
        Map<String, String> settingsMap = new HashMap<>();
        for (String elementKey : elements.keySet()) {
            String elementValue = elements.get(elementKey).getAsString();
            settingsMap.put(elementKey, elementValue);
        }
        return settingsMap;
    }

    public static boolean save(Map<String, Map<String, InputField>> settings) {
        JsonObject allSettings = new JsonObject();
        for (String key : settings.keySet()) {
            Map<String, InputField> setting = settings.get(key);
            JsonObject innerSettings = new JsonObject();
            for (String settingKey : setting.keySet()) {
                innerSettings.addProperty(settingKey, setting.get(settingKey).getValue());
            }
            allSettings.add(key, innerSettings);
        }

        try (PrintWriter writer = new PrintWriter(SETTINGS_PATH)) {
            writer.write(allSettings.toString());
            writer.flush();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }
}
