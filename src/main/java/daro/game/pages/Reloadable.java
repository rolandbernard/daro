package daro.game.pages;

/**
 * A simple interface defining a method called reload, that reloads
 * the main content of the page
 *
 * @author Daniel Planötscher
 */
public interface Reloadable {
    /**
     * Reloads the main content of the page, that might change during the usage of the game
     */
    void reload();
}
