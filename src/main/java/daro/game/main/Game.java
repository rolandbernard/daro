package daro.game.main;

import daro.game.pages.CoursePage;
import daro.game.ui.Navigation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class Game extends Application {

    @Override
    public void start(Stage stage) {

        HBox layout = new HBox(new Navigation(), GameHelper.getContainer());
        GameHelper.updateContainer(new CoursePage());
        Scene scene = new Scene(layout, GameHelper.GAME_WIDTH, GameHelper.GAME_HEIGHT);

        //load CSS
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap");
        scene.getStylesheets().add("https://fonts.googleapis.com/icon?family=Material+Icons");
        scene.getStylesheets().add("styles/index.css");

        stage.setTitle("Learn programming with daro!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
