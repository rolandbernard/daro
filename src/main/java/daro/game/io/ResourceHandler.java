package daro.game.io;

import javafx.scene.image.Image;

import java.io.*;

/**
 * Utility class: Helps accessing the maven resource folder (even when packaged)
 *
 * @author Daniel Planötscher
 */
public final class ResourceHandler {
    private static final String RESOURCE_ROOT = "/daro/game/";

    private ResourceHandler() {
        // Disallow instantiation
    }

    /**
     * Returns the Font-File as an InputStream
     *
     * @param filename name of the font-file
     * @return the Font-File as InputStream
     */
    public static InputStream getFont(String filename) {
        return ResourceHandler.class.getResourceAsStream(RESOURCE_ROOT + "fonts/" + filename);
    }

    /**
     * Returns a stylesheet file that can be added to a scene
     *
     * @param filename the name of the css file
     * @return a String containing the url to the file
     */
    public static String getStyleSheet(String filename) {
        return ResourceHandler.class.getResource(RESOURCE_ROOT + "styles/" + filename).toExternalForm();
    }

    /**
     * Returns a JavaFX image to that can be used directly in the app.
     *
     * @param filename the name of the image file
     * @return a JavaFX Image Object
     */
    public static Image getImage(String filename) {
        return new Image(ResourceHandler.class.getResource(RESOURCE_ROOT + "img/" + filename).toExternalForm());
    }

    /**
     * Returns the content of a data file in the game resources
     *
     * @param filename a json filename
     * @return a string containing the file contents
     */
    public static String getDataContent(String filename) {
        InputStream path = ResourceHandler.class.getResourceAsStream(RESOURCE_ROOT + "data/" + filename);
        try {
            return IOHelpers.getFileContent(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
