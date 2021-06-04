package daro.game.main;

import daro.game.pages.CoursePage;
import daro.game.views.MenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Game extends Application {
    public static final double HEIGHT = 720;
    public static final double WIDTH = 1280;

    public static void init(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // load fonts from resources
        Font.loadFont(PathHandler.getFont("Montserrat-Regular.ttf"), 16);
        Font.loadFont(PathHandler.getFont("Montserrat-Italic.ttf"), 16);
        Font.loadFont(PathHandler.getFont("Montserrat-Bold.ttf"), 16);
        Font.loadFont(PathHandler.getFont("Montserrat-BoldItalic.ttf"), 16);
        Font.loadFont(PathHandler.getFont("MaterialIcons-Regular.ttf"), 24);
        Font.loadFont(PathHandler.getFont("JetBrainsMono-Bold.ttf"), 16);
        Font.loadFont(PathHandler.getFont("JetBrainsMono-BoldItalic.ttf"), 16);
        Font.loadFont(PathHandler.getFont("JetBrainsMono-Regular.ttf"), 16);
        Font.loadFont(PathHandler.getFont("JetBrainsMono-Italic.ttf"), 16);

        // generate start page
        MenuView root = new MenuView(new CoursePage());
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // load CSS
        try {
            scene.getStylesheets().add(PathHandler.getStyleSheet("index.css"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.setTitle("Learn programming with daro!");
        stage.setScene(scene);
        stage.show();
    }
}
