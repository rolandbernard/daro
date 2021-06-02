package daro.game.main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.util.Scanner;

public class PathHandler {
    private static final String RESOURCE_ROOT = "/game/";

    /**
     * Returns the Font-File as an InputStream
     *
     * @param filename name of the font-file
     * @return the Font-File as InputStream
     */
    public static InputStream getFont(String filename) {
        return PathHandler.class.getResourceAsStream(RESOURCE_ROOT + "fonts/" + filename);
    }

    /**
     * Returns a stylesheet file that can be added to a scene
     * @param filename the name of the css file
     * @return a String containing the url to the file
     */
    public static String getStyleSheet(String filename) {
        return PathHandler.class.getResource(RESOURCE_ROOT + "styles/" + filename).toExternalForm();
    }

    /**
     * Returns a JsonObject from a directory
     *
     * @param filename a json filename
     * @return a JsonObject parsed from the file
     * @throws ParseException
     */
    public static JSONObject getJsonData(String filename) throws ParseException {
        Scanner scanner = new Scanner(PathHandler.class.getResourceAsStream(RESOURCE_ROOT + "/data/" + filename));
        scanner.useDelimiter("\\Z");
        JSONParser parser = new JSONParser();
        Object object = parser.parse(scanner.next());
        scanner.close();
        return (JSONObject) object;
    }
}
