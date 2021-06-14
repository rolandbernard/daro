package daro.game.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import daro.game.ui.fields.InputField;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public abstract class SettingsHandler {

    private static String SETTINGS_PATH = UserData.USER_PATH + "settings.json";

    private static JsonObject getSettings() {
        return UserData.parseUserJson("settings.json");
    }

    public static Map<String, Map<String, JsonElement>> getAllSettings() {
        JsonObject settings = getSettings();
        Map<String, Map<String, JsonElement>> settingsMap = new HashMap<>();
        if (settings != null) {
            for (String key : settings.keySet()) {
                JsonObject elements = settings.get(key).getAsJsonObject();
                Map<String, JsonElement> keyValue = generateMapFromJson(elements);
                settingsMap.put(key, keyValue);
            }
        }
        return settingsMap;
    }

    public static Map<String, JsonElement> getSettingsByKey(String key) {
        JsonObject settings = getSettings();
        Map<String, JsonElement> settingsMap = new HashMap<>();
        if (settings != null) {
            JsonObject elements = settings.get(key).getAsJsonObject();
            settingsMap = generateMapFromJson(elements);
        }
        return settingsMap;
    }

    private static Map<String, JsonElement> generateMapFromJson(JsonObject elements) {
        Map<String, JsonElement> settingsMap = new HashMap<>();
        for (String elementKey : elements.keySet()) {
            JsonElement elementValue = elements.get(elementKey);
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
                Object value = setting.get(settingKey).getValue();
                if (value instanceof Boolean) {
                    innerSettings.addProperty(settingKey, (boolean)value);
                } else if (value instanceof String) {
                    innerSettings.addProperty(settingKey, (String)value);
                } else if (value instanceof Number) {
                    innerSettings.addProperty(settingKey, (Number)value);
                }
            }
            allSettings.add(key, innerSettings);
        }

        try (PrintWriter writer = new PrintWriter(SETTINGS_PATH)) {
            writer.write(allSettings.toString());
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }
}
