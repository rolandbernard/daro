package daro.game.io;

import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;

/**
 * Utility class: Helps accessing the user specific folder of the game.
 *
 * @author Daniel Planötscher
 */
public final class UserData {

    private UserData() {
        // Disallow instantiation
    }

    static final String USER_PATH = "./user/";

    /**
     * Parses JSON-Files from the user files
     *
     * @param filename the name of the file you want to access
     * @return the parsed JsonObject
     */
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
}
