package daro.game.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import daro.game.main.Challenge;
import daro.game.validation.Validation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class ChallengeHandler {
    private static final String CHALLENGE_PATH = UserData.USER_PATH + "challenges/";

    public static void importChallenge(File file) {
        try {
            String newName = generateUniqueName();
            Files.copy(file.toPath(), Path.of(CHALLENGE_PATH + newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean removeChallenge(File file) {
        System.out.println(file);
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
                    Scanner scanner = new Scanner(file);
                    scanner.useDelimiter("\\Z");
                    JsonObject obj = JsonParser.parseString(scanner.next()).getAsJsonObject();
                    challenges.add(parseChallenge(file, obj));
                    scanner.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return challenges;
    }

    /**
     * Unique filenames are possible by using the current timestamp.
     *
     * @return a string with a unique filename
     */
    private static String generateUniqueName() {
        return new Date().getTime() + ".json";
    }

    public static Challenge parseChallenge(File source, JsonObject challengeObj) {
        String name = challengeObj.get("name").getAsString();
        String creator = challengeObj.get("creator").getAsString();
        String description = challengeObj.get("description").getAsString();
        JsonArray tests = challengeObj.get("tests") != null ? challengeObj.get("tests").getAsJsonArray() : null;
        List<Validation> testsList = LevelHandler.parseValidationsFromJson(tests);
        String standardCode = challengeObj.get("startCode") == null ? "" : challengeObj.get("startCode").getAsString();
        return new Challenge(name, description, standardCode, testsList, creator, source);
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
                    Scanner scanner = new Scanner(file);
                    scanner.useDelimiter("\\Z");
                    JsonObject obj = JsonParser.parseString(scanner.next()).getAsJsonObject();
                    Challenge oldChallenge = parseChallenge(file, obj);
                    scanner.close();
                    if (oldChallenge.isSimilar(newChallenge)) {
                        PrintWriter writer = new PrintWriter(file.getPath());
                        writer.write(newJsonObj.toString());
                        writer.flush();
                        writer.close();
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    public static JsonObject serializeChallenge(String name, String creator, String description, String code, List<Map<String, String>> tests) {
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
