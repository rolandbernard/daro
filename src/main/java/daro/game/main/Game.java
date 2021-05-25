package daro.game.main;

import daro.game.pages.CoursePage;
import daro.game.views.MenuView;
import daro.game.views.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Game extends Application {
    public static final double HEIGHT = 720;
    public static final double WIDTH = 1280;
    private Scene scene;

    public static void init(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        //load fonts from resources
        Font.loadFont(Main.class.getResourceAsStream("/fonts/Montserrat-Regular.ttf"), 16);
        Font.loadFont(Main.class.getResourceAsStream("/fonts/Montserrat-Italic.ttf"), 16);
        Font.loadFont(Main.class.getResourceAsStream("/fonts/Montserrat-Bold.ttf"), 16);
        Font.loadFont(Main.class.getResourceAsStream("/fonts/Montserrat-BoldItalic.ttf"), 16);
        Font.loadFont(Main.class.getResourceAsStream("/fonts/MaterialIcons-Regular.ttf"), 16);

        //generate start page
        MenuView root = new MenuView(new CoursePage());
        scene = new Scene(root, WIDTH, HEIGHT);

        // load CSS
        try {
            scene.getStylesheets().add(this.getClass().getResource("/styles/index.css").toExternalForm());
        } catch(Exception e) {
            e.printStackTrace();
        }

        stage.setTitle("Learn programming with daro!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * Updates the view of the Game to a given page.
     * 
     * @param view
     *            The view game displays
     *
     */
    public void setView(View view) {
        scene.setRoot(view);
    }
}
