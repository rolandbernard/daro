package daro.game.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Utility class:
 * Helps creating and reading files, to prevent issues.
 *
 * @author Daniel Planötscher
 */
public final class IOHelpers {

    private IOHelpers() {
        // Disallow instantiation
    }

    /**
     * Overwrite an old file with new content
     *
     * @param file the file object of the file you want to overwrite
     * @param content the new content
     * @throws FileNotFoundException should be handled when called
     */
    public static void overwriteFile(File file, String content) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    /**
     * Gets the content of a file
     *
     * @param file the file you want to read
     * @return a string containing its contents
     * @throws FileNotFoundException should be handled in the code directly
     */
    public static String getFileContent(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("\\Z");
        String content = getContent(scanner);
        scanner.close();
        return content;
    }

    /**
     * Gets the content of a file
     *
     * @param ioStream the InputStream you want to read
     * @return a string containing its contents
     */
    public static String getFileContent(InputStream ioStream) {
        Scanner scanner = new Scanner(ioStream);
        scanner.useDelimiter("\\Z");
        String content = getContent(scanner);
        scanner.close();
        return content;
    }

    /**
     * Simple helper to get the content of a scanner
     *
     * @param scanner the of the string
     * @return a string containing the read content, or being empty
     */
    private static String getContent(Scanner scanner) {
        return scanner.hasNext() ? scanner.next() : "";
    }
}
