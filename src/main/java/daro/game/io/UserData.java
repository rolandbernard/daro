package daro.game.io;

import com.google.gson.*;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class UserData {
    static final String USER_PATH = "./user/";
    private static final String PLAYGROUNDS_PATH = USER_PATH + "playgrounds";

    // LEVELS
    public static JsonObject parseUserJson(String filename) {
        File file = new File(USER_PATH + filename);
        JsonObject element = new JsonObject();
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\\Z");
            if (scanner.hasNext()) {
                element = JsonParser.parseString(scanner.next()).getAsJsonObject();
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return element;
    }

    public static Map<Long, JsonObject> getLevelGroupData(long groupId) {
        JsonObject object = parseUserJson("data.json");
        Map<Long, JsonObject> map = new HashMap<>();
        JsonElement groupData = object.get(String.valueOf(groupId));

        if (groupData != null) {
            JsonArray groups = groupData.getAsJsonArray();
            for (JsonElement level : groups) {
                JsonObject obj = level.getAsJsonObject();
                map.put(obj.get("id").getAsLong(), obj);
            }
        }
        return map;
    }


    /**
     * A method that rewrites the user data file by either creating or replacing an old entry of
     * user data concerning the completion of a level.
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

    //Playgrounds

    /**
     * Creates, if it not already exists, the playgrounds folder and parses the
     * files
     *
     * @return an array of playground files
     */
    public static File[] parsePlaygrounds() {
        File playgrounds = new File(PLAYGROUNDS_PATH);
        if (!playgrounds.exists()) {
            try {
                Files.createDirectories(Path.of(PLAYGROUNDS_PATH));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return playgrounds.listFiles();
    }

    /**
     * Create a new Playgroundfile
     *
     * @param name name of the new Playground (without .daro)
     * @return a string being null (if successful) or with an error message
     */
    public static String createPlayground(String name) {
        Path newFile = Path.of(PLAYGROUNDS_PATH + "/" + name + ".daro");
        try {
            Files.createFile(newFile);
            return null;
        } catch (FileAlreadyExistsException e) {
            return "This playground already exists. Please choose a different name.";
        } catch (IOException e) {
            return "There was an issue with creating this playground. Please try again.";
        }
    }

    /**
     * Removes a Playgroundfile
     *
     * @param filename filename with .daro
     * @return true if successful, false if error
     */
    public static boolean removePlayground(String filename) {
        Path file = Path.of(PLAYGROUNDS_PATH + "/" + filename);
        try {
            Files.delete(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Get content of a Playgroundfile
     *
     * @param file Playground file
     * @return true if successful, false if error
     * @throws IOException TODO TOFIX
     */
    public static String getPlayground(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("\\Z");
        if (scanner.hasNext()) {
            return scanner.next();
        }
        return "";
    }

    /**
     * Get a playground File
     *
     * @param filename Playground filename with .daro
     * @return a file object
     */
    public static File getPlaygroundFile(String filename) throws IOException {
        return new File(PLAYGROUNDS_PATH + "/" + filename);
    }

    /**
     * Update playground file
     *
     * @param file playground file
     * @param code     TODO TOFIX
     * @return true if successful, false if error
     */
    public static boolean savePlayground(File file, String code) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(code);
            writer.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
