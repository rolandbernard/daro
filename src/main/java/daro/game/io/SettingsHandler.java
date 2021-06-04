package daro.game.io;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public abstract class SettingsHandler {

    private static JsonObject getSettings() {
        return UserData.parseUserJson("settings.json");
    }

    public static Map<String, String> getAllSettings() {
        JsonObject settings = getSettings();
        Map<String, String> settingsMap = new HashMap<>();
        return settingsMap;
    }
}
