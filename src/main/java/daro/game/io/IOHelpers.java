package daro.game.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public final class IOHelpers {

    private IOHelpers() {
        // Disallow instantiation
    }

    public static void overwriteFile(File file, String content) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    public static String getFileContent(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("\\Z");
        String content = getContent(scanner);
        scanner.close();
        return content;
    }

    public static String getFileContent(InputStream ioStream) {
        Scanner scanner = new Scanner(ioStream);
        scanner.useDelimiter("\\Z");
        String content = getContent(scanner);
        scanner.close();
        return content;
    }

    private static String getContent(Scanner scanner) {
        return scanner.hasNext() ? scanner.next() : "";
    }
}
