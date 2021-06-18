package daro.game.ui;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * <strong>UI: <em>Helper</em></strong><br>
 * Helps handling animations for user interactions
 *
 * @author Daniel Planötscher
 */
public final class Interaction {
    private static final Duration DURATION = new Duration(300);

    private Interaction() {
        // Disallow instantiation
    }

    /**
     * Starts an animation, moving the node up
     *
     * @param n node that should move
     */
    public static void translateUp(Node n) {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(DURATION);
        transition.setByY(-5);
        transition.setNode(n);
        transition.setAutoReverse(true);
        transition.play();
    }

    /**
     * Starts an animation, moving the node down
     *
     * @param n node that should move
     */
    public static void translateBack(Node n) {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(DURATION);
        transition.setToY(0);
        transition.setNode(n);
        transition.play();
    }

    /**
     * Starts an animation, scaling the node down
     *
     * @param n node that should scale
     */
    public static void scaleDown(Node n) {
        ScaleTransition transition = new ScaleTransition();
        transition.setDuration(DURATION);
        transition.setToX(0.95);
        transition.setToY(0.95);
        transition.setNode(n);
        transition.play();
    }

    /**
     * Starts an animation, scaling the node back to normal
     *
     * @param n node that should scale
     */
    public static void scaleBack(Node n) {
        ScaleTransition transition = new ScaleTransition();
        transition.setDuration(DURATION);
        transition.setToX(1);
        transition.setToY(1);
        transition.setNode(n);
        transition.play();
    }

    /**
     * Starts an animation, scaling the node from 0 to 1
     *
     * @param n node that should scale
     */
    public static void scaleIn(Node n) {
        ScaleTransition transition = new ScaleTransition();
        transition.setDuration(DURATION);
        transition.setFromX(0);
        transition.setFromY(0);
        transition.setToX(1);
        transition.setToY(1);
        transition.setNode(n);
        transition.play();

    }

    /**
     * Makes elements feel more clickable.
     *
     * @param n node that should feel clickable
     * @param moveUp if it should move up when hovering
     */
    public static void setClickable(Node n, boolean moveUp) {
        n.setOnMousePressed(e -> Interaction.scaleDown(n));
        n.setOnMouseReleased(e -> Interaction.scaleBack(n));
        if (moveUp) {
            n.setOnMouseEntered(e -> Interaction.translateUp(n));
            n.setOnMouseExited(e -> Interaction.translateBack(n));
        }
        n.setCursor(Cursor.HAND);
    }
}
