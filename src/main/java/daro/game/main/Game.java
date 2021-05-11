package daro.game.main;

import daro.game.ui.Navigation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Game extends Application {

    @Override
    public void start(Stage stage) {

        VBox content = new VBox();
        content.setPrefHeight(720);
        content.setPrefWidth(960);
        content.setStyle("-fx-background-color: #200D57");
        HBox layout = new HBox(new Navigation(), content);
        Scene scene = new Scene(layout, 1280, 720);
        try {
            scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap");
            scene.getStylesheets().add("https://fonts.googleapis.com/icon?family=Material+Icons");
            scene.getStylesheets().add("styles/index.css");
        } catch(NullPointerException e) {
            System.out.println("\n\nThere was an error with loading the CSS");
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
