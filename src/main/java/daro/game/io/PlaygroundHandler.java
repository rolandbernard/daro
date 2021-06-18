package daro.game.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class: Helps handling Playgrounds.
 *
 * @author Daniel Planötscher
 */
public final class PlaygroundHandler {

    private PlaygroundHandler() {
        // Disallow instantiation
    }

    private static final String PLAYGROUNDS_PATH = UserData.USER_PATH + "playgrounds/";

    /**
     * Creates, if it not already exists, the playgrounds folder and parses the
     * files
     *
     * @return an array of playground files
     */
    public static File[] parsePlaygrounds() {
        File playgrounds = new File(PLAYGROUNDS_PATH);
        if (!playgrounds.exists() && !playgrounds.isDirectory()) {
            try {
                Files.createDirectories(Path.of(PLAYGROUNDS_PATH));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return playgrounds.listFiles();
    }

    /**
     * Create a new Playground file
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
     * Removes a Playground file
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
     * Get content of a Playground file
     *
     * @param file Playground file
     * @return true if successful, false if error
     * @throws IOException error while accessing the file
     */
    public static String getPlayground(File file) throws IOException {
        return IOHelpers.getFileContent(file);
    }

    /**
     * Get a playground File
     *
     * @param filename Playground filename with .daro
     * @return a file object
     * @throws IOException error while accessing the file
     */
    public static File getPlaygroundFile(String filename) throws IOException {
        return new File(PLAYGROUNDS_PATH + "/" + filename);
    }

    /**
     * Update playground file
     *
     * @param file playground file
     * @param code the code of the playground
     * @return true if successful, false if error
     */
    public static boolean savePlayground(File file, String code) {
        try {
            IOHelpers.overwriteFile(file, code);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
