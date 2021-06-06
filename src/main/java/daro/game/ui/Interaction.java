package daro.game.ui;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class Interaction {
    private static final Duration DURATION = new Duration(300);

    /**
     * Starts an animation, moving the node up
     *
     * @param n node that should move
     */
    public static void translateUp(Node n) {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(DURATION);
        transition.setByY(-10);
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

    public static void scaleDown(Node n) {
        ScaleTransition transition = new ScaleTransition();
        transition.setDuration(DURATION);
        transition.setByX(-0.05);
        transition.setByY(-0.05);
        transition.setNode(n);
        transition.play();
    }

    public static void scaleBack(Node n) {
        ScaleTransition transition = new ScaleTransition();
        transition.setDuration(DURATION);
        transition.setToX(1);
        transition.setToY(1);
        transition.setNode(n);
        transition.play();
    }

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
}
