package daro.game.main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Game extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new StackPane(new Label("JavaFX up and running")), 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
