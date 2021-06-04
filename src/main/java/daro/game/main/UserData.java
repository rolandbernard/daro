package daro.game.main;

import javafx.scene.input.MouseEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class UserData {
    private static final String USER_PATH = "./user/";
    private static final String PLAYGROUNDS_PATH = USER_PATH + "playgrounds";

    // LEVELS
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

    public static Map<Long, JSONObject> getLevelGroupData(long groupId) {
        JSONObject object = parseUserDataJson();
        Map<Long, JSONObject> map = new HashMap<>();
        Object levelGroupData = object.get(String.valueOf(groupId));
        if (levelGroupData != null) {
            JSONArray groupData = (JSONArray) levelGroupData;
            for (Object level : groupData) {
                JSONObject levelData = (JSONObject) level;
                map.put((long) levelData.get("id"), levelData);
            }
        }
        return map;
    }

    /**
     * A method that rewrites the user data file by either creating or replacing an old entry of
     * user data concerning the completion of a level.
     *
     * @param groupId the group id of the level
     * @param levelId the level id
     * @param completion the completion (if it was solved successfully)
     * @param currentCode the code written by the user
     * @return if the writing wsa successful or not.
     */
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
            boolean existsAlready = false;
            for (Object o : levelGroup) {
                JSONObject level = (JSONObject) o;
                if ((long) level.get("id") == levelId) {
                    level.replace("completed", completion);
                    level.replace("currentCode", currentCode);
                    existsAlready = true;
                    break;
                }
            }
            if(!existsAlready) {
                JSONObject level = new JSONObject();
                level.put("id", levelId);
                level.put("completed", completion);
                level.put("currentCode", currentCode);
                levelGroup.add(level);
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

    //Playgrounds

    /**
     * Creates, if it not already exists, the playgrounds folder and parses the files
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
     * @param filename filename with .daro
     * @return true if successful, false if error
     */
    public static String getPlayground(String filename) throws IOException {
        File file = new File(PLAYGROUNDS_PATH + "/" + filename);
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("\\Z");
        if (scanner.hasNext()) {
            return scanner.next();
        }
        return "";
    }

    /**
     * Get content of a Playgroundfile
     *
     * @param filename filename with .daro
     * @return true if successful, false if error
     */
    public static boolean savePlayground(String filename, String code) {
        try (PrintWriter file = new PrintWriter(PLAYGROUNDS_PATH + "/" + filename)) {
            file.write(code);
            file.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
