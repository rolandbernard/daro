package daro.game.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import daro.game.ui.fields.InputField;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class:
 * Helps accessing and modifying the settings file.
 *
 * @author Daniel Planötscher
 */
public final class SettingsHandler {

    private SettingsHandler() {
        // Disallow instantiation
    }

    private final static String SETTINGS_PATH = UserData.USER_PATH + "settings.json";

    /**
     * Gets the JsonObject of the settings
     *
     * @return a JsonObject of all current settings
     */
    private static JsonObject getSettings() {
        return UserData.parseUserJson("settings.json");
    }

    /**
     * Parses all settings into a usable Map
     *
     * @return a Map that can be used more easily in Java
     */
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

    /**
     * Only returns a specific group of settings (e.g. only editor settings)
     *
     * @param key the key of the SettingsGroup
     * @return a Map that can be used by more easily in Java
     */
    public static Map<String, JsonElement> getSettingsByKey(String key) {
        JsonObject settings = getSettings();
        Map<String, JsonElement> settingsMap = new HashMap<>();
        if (settings != null) {
            if (settings.get(key) != null) {
                JsonObject elements = settings.get(key).getAsJsonObject();
                settingsMap = generateMapFromJson(elements);
            }
        }
        return settingsMap;
    }

    /**
     * Generates a usable Java Map from a JsonObject
     *
     * @param elements the JsonObject you want to convert
     * @return the key value pairs
     */
    private static Map<String, JsonElement> generateMapFromJson(JsonObject elements) {
        Map<String, JsonElement> settingsMap = new HashMap<>();
        for (String elementKey : elements.keySet()) {
            JsonElement elementValue = elements.get(elementKey);
            settingsMap.put(elementKey, elementValue);
        }
        return settingsMap;
    }

    /**
     * Overwrites the current settings with the current inputs
     *
     * @param settings a map containing the current settings
     * @return successfulness of saving
     */
    public static boolean save(Map<String, Map<String, InputField>> settings) {
        JsonObject allSettings = new JsonObject();
        for (String key : settings.keySet()) {
            Map<String, InputField> setting = settings.get(key);
            JsonObject innerSettings = new JsonObject();
            for (String settingKey : setting.keySet()) {
                Object value = setting.get(settingKey).getValue();
                if (value instanceof Boolean) {
                    innerSettings.addProperty(settingKey, (boolean) value);
                } else if (value instanceof String) {
                    innerSettings.addProperty(settingKey, (String) value);
                } else if (value instanceof Number) {
                    innerSettings.addProperty(settingKey, (Number) value);
                }
            }
            allSettings.add(key, innerSettings);
        }

        try {
            File file = new File(SETTINGS_PATH);
            IOHelpers.overwriteFile(file, allSettings.toString());
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
