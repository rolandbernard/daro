package daro.game.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ChallengeHandler {
    private static String CHALLENGE_PATH = UserData.USER_PATH + "challenges/";
    public static Path importChallenge(File file) {
        try {
            File challengePath = new File(CHALLENGE_PATH);
            if(!challengePath.exists()) {
                Files.createDirectory(challengePath.toPath());
            }
            return Files.copy(file.toPath(), Path.of(CHALLENGE_PATH + file.getName()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
