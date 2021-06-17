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

public final class ChallengeHandler {
    private static final String CHALLENGE_PATH = UserData.USER_PATH + "challenges/";

    private ChallengeHandler() {
    }

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

    public static boolean removeChallenge(File file) {
        try {
            Files.delete(file.toPath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

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
                    e.printStackTrace();
                }
            }
        }
        return challenges;
    }

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

    public static boolean hasSimilar(Challenge c) {
        return getImportedChallenges().stream().anyMatch(c::isSimilar);
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
