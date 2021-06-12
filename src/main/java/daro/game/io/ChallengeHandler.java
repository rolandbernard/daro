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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public abstract class ChallengeHandler {
    private static String CHALLENGE_PATH = UserData.USER_PATH + "challenges/";

    public static Path importChallenge(File file) {
        try {
            String newName = generateUniqueName();
            return Files.copy(file.toPath(), Path.of(CHALLENGE_PATH + newName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
                    challenges.add(parseChallenge(obj));
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

    public static Challenge parseChallenge(JsonObject challengeObj) {
        String name = challengeObj.get("name").getAsString();
        String creator = challengeObj.get("creator").getAsString();
        String description = challengeObj.get("description").getAsString();
        JsonArray tests = challengeObj.get("tests") != null ? challengeObj.get("tests").getAsJsonArray() : null;
        List<Validation> testsList = LevelHandler.parseValidationsFromJson(tests);
        String standardCode = challengeObj.get("startCode") == null ? "" : challengeObj.get("startCode").getAsString();
        return new Challenge(name, description, standardCode, testsList, creator);
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
                    Challenge oldChallenge = parseChallenge(obj);
                    if(oldChallenge.isSimilar(newChallenge)) {
                        PrintWriter writer = new PrintWriter(file.getPath());
                        writer.write(newJsonObj.toString());
                        writer.flush();
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
