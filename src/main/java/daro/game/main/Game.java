package daro.game.main;

import daro.game.pages.CoursePage;
import daro.game.ui.Container;
import daro.game.ui.Navigation;
import daro.game.ui.Page;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class Game extends Application {
    private final static Container CONTENT = new Container();
    public static final double HEIGHT = 720, WIDTH = 1280;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        HBox layout = new HBox(new Navigation(), CONTENT);
        Scene scene = new Scene(layout, WIDTH, HEIGHT);

        //load CSS
        //TODO: check fonts
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap");
        scene.getStylesheets().add("https://fonts.googleapis.com/icon?family=Material+Icons");
        scene.getStylesheets().add("styles/index.css");


        stage.setTitle("Learn programming with daro!");
        stage.setScene(scene);
        stage.show();
    }

    public static void setContent(Page n) {
        CONTENT.setContent(n);
    }

}
