package daro.game.io;

import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;

public final class UserData {

    private UserData() {
        // Disallow instantiation
    }

    static final String USER_PATH = "./user/";

    // LEVELS
    public static JsonObject parseUserJson(String filename) {
        File file = new File(USER_PATH + filename);
        JsonObject element = new JsonObject();
        try {
            String jsonContent = IOHelpers.getFileContent(file);
            if (!jsonContent.isEmpty()) {
                element = JsonParser.parseString(jsonContent).getAsJsonObject();
            }
        } catch (IOException ex) {
            try {
                File userDir = new File(USER_PATH);
                if (!userDir.exists() || !userDir.isDirectory()) {
                    Files.createDirectories(userDir.toPath());
                }
                IOHelpers.overwriteFile(file, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return element;
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
        JsonObject outputObject = parseUserJson("data.json");
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
        try (PrintWriter file = new PrintWriter(USER_PATH + "data.json")) {
            file.write(outputObject.toString());
            file.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static JsonObject createLevelDataEntry(long levelId, boolean completion, String currentCode) {
        JsonObject level = new JsonObject();
        level.addProperty("id", levelId);
        level.addProperty("completed", completion);
        level.addProperty("currentCode", currentCode);
        return level;
    }

}
