package daro.game.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Scanner;

public abstract class PathHandler {
    private static final String RESOURCE_ROOT = "/daro/game/";

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
     * 
     * @param filename the name of the css file
     * @return a String containing the url to the file
     */
    public static String getStyleSheet(String filename) {
        return PathHandler.class.getResource(RESOURCE_ROOT + "styles/" + filename).toExternalForm();
    }

    /**
     * Returns a JavaFX image to that can be used directly in the app.
     *
     * @param filename the name of the image file
     * @return a JavaFX Image Object
     */
    public static Image getImage(String filename) {
        return new Image(PathHandler.class.getResource(RESOURCE_ROOT + "img/" + filename).toExternalForm());
    }

    /**
     * Returns a JsonObject from a directory
     *
     * @param filename a json filename
     * @return a JsonObject parsed from the file
     */
    public static JsonElement getJsonData(String filename) {
        Scanner scanner = new Scanner(PathHandler.class.getResourceAsStream(RESOURCE_ROOT + "/data/" + filename));
        scanner.useDelimiter("\\Z");
        JsonElement element;
        if (scanner.hasNext()) {
            element = JsonParser.parseReader(new StringReader(scanner.next()));
        } else {
            element = new JsonObject();
        }
        scanner.close();
        return element;
    }
}
