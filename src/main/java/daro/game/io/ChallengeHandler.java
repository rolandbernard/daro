package daro.game.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import daro.game.parser.ChallengeParser;
import daro.game.main.Challenge;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Utility class:
 * Handles many aspects of challenges, such as its serialization, importing etc.
 *
 * @author Daniel Planötscher
 */
public final class ChallengeHandler {
    private static final String CHALLENGE_PATH = UserData.USER_PATH + "challenges/";

    private ChallengeHandler() {
    }

    /**
     * Gets all imported challenges
     *
     * @return a list containing the parsed challenges
     */
    public static List<Challenge> getImportedChallenges() {
        File challengesFolder = new File(CHALLENGE_PATH);
        ArrayList<Challenge> challenges = new ArrayList<>();
        if (!challengesFolder.exists() && !challengesFolder.isDirectory()) {
            try {
                Files.createDirectories(Path.of(CHALLENGE_PATH));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        File[] challengeFiles = challengesFolder.listFiles();
        if (challengeFiles != null) {
            for (File file : challengeFiles) {
                try {
                    String content = IOHelpers.getFileContent(file);
                    challenges.add(ChallengeParser.parse(content, file));
                } catch (IOException e) {
                    return null;
                }
            }
        }
        return challenges;
    }

    /**
     * Imports a new challenge into the challenge folder
     *
     * @param file challengeFile
     * @return the new imported file
     */
    public static File importChallenge(File file) {
        try {
            String newName = generateUniqueName();
            Files.copy(file.toPath(), Path.of(CHALLENGE_PATH + newName));
            return new File(CHALLENGE_PATH + newName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes a challenge from the challenge folder
     *
     * @param file the file of the challenge
     * @return successfulness of the deletion
     */
    public static boolean removeChallenge(File file) {
        try {
            Files.delete(file.toPath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves the current state of a challenge
     *
     * @param challenge  the challenge object you want to update
     * @param code       the updated code of the update
     * @param completion the completion of the new code
     * @return if the operation was successful or not
     */
    public static boolean saveChallenge(Challenge challenge, String code, boolean completion) {
        try {
            String jsonString = IOHelpers.getFileContent(challenge.getSourceFile());
            JsonObject obj = JsonParser.parseString(jsonString).getAsJsonObject();
            obj.addProperty("currentCode", code);
            obj.addProperty("completion", completion);
            IOHelpers.overwriteFile(challenge.getSourceFile(), obj.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Unique filenames are possible by using the current timestamp.
     *
     * @return a string with a unique filename
     */
    private static String generateUniqueName() {
        return new Date().getTime() + ".json";
    }

    /**
     * Checks if the given challenge has a similar one.
     *
     * @param challenge the challenge you want to check
     * @return if it has a similar one
     */
    public static boolean hasSimilar(Challenge challenge) {
        return getImportedChallenges() != null && getImportedChallenges().stream().anyMatch(challenge::isSimilar);
    }

    public static boolean replaceSimilar(Challenge newChallenge, JsonObject newJsonObj) {
        File challengesFolder = new File(CHALLENGE_PATH);
        File[] challengeFiles = challengesFolder.listFiles();
        if (challengeFiles != null) {
            for (File file : challengeFiles) {
                try {
                    String content = IOHelpers.getFileContent(file);
                    Challenge oldChallenge = ChallengeParser.parse(content, file);
                    if (oldChallenge.isSimilar(newChallenge)) {
                        IOHelpers.overwriteFile(file, newJsonObj.toString());
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Serializes a challenge for creation
     *
     * @param name        name of the challenge
     * @param creator     creator of the challenge
     * @param description description of the challenge
     * @param code        standard code of the challenge
     * @param tests       a list containing its tests
     * @return the new JsonObject for the challenge
     */
    public static JsonObject serializeChallenge(
            String name, String creator, String description, String code, List<Map<String, String>> tests
    ) {
        JsonObject object = new JsonObject();
        object.addProperty("name", name);
        object.addProperty("creator", creator);
        object.addProperty("description", description);
        object.addProperty("startCode", code);
        JsonArray testsArray = new JsonArray();
        int i = 1;
        for (Map<String, String> map : tests) {
            JsonObject test = new JsonObject();
            test.addProperty("id", i);
            i++;
            for (String key : map.keySet()) {
                test.addProperty(key, map.get(key));
            }
            testsArray.add(test);
        }
        object.add("tests", testsArray);
        return object;
    }
}
