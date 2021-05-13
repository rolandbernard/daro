package daro.game.main;

import daro.game.pages.CoursePage;
import daro.game.views.MenuView;
import daro.game.views.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Game extends Application {
    public static final double HEIGHT = 720, WIDTH = 1280;
    private static final Scene SCENE = new Scene(new MenuView(new CoursePage()), WIDTH, HEIGHT);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        //load CSS
        //TODO: check font paths
        SCENE.getStylesheets().add("https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap");
        SCENE.getStylesheets().add("https://fonts.googleapis.com/icon?family=Material+Icons");
        SCENE.getStylesheets().add("styles/index.css");


        stage.setTitle("Learn programming with daro!");
        stage.setScene(SCENE);
        stage.show();
    }


    /**
     *
     * Updates the view of the Game to a given page.
     * @param view The view game displays
     *
     */
    public static void setView(View view) {
        SCENE.setRoot(view);
    }

}
