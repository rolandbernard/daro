package daro.game.main;

import daro.game.pages.CoursePage;
import daro.game.views.MenuView;
import daro.game.views.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Game extends Application {
    public static final double HEIGHT = 720;
    public static final double WIDTH = 1280;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        //load CSS
        //TODO: check font paths
        MenuView root = new MenuView(new CoursePage());

        scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap");
        scene.getStylesheets().add("https://fonts.googleapis.com/icon?family=Material+Icons");
        scene.getStylesheets().add("styles/index.css");

        stage.setTitle("Learn programming with daro!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * Updates the view of the Game to a given page.
     * @param view The view game displays
     *
     */
    public void setView(View view) {
        scene.setRoot(view);
    }
}
