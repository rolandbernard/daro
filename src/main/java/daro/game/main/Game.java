package daro.game.main;

import daro.game.io.ResourceHandler;
import daro.game.pages.CoursePage;
import daro.game.views.MenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Game extends Application {

    public static void init(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // load fonts from resources
        Font.loadFont(ResourceHandler.getFont("Montserrat-Regular.ttf"), 16);
        Font.loadFont(ResourceHandler.getFont("Montserrat-Bold.ttf"), 16);
        Font.loadFont(ResourceHandler.getFont("MaterialIcons-Regular.ttf"), 24);
        Font.loadFont(ResourceHandler.getFont("JetBrainsMono-Bold.ttf"), 16);
        Font.loadFont(ResourceHandler.getFont("JetBrainsMono-BoldItalic.ttf"), 16);
        Font.loadFont(ResourceHandler.getFont("JetBrainsMono-Regular.ttf"), 16);
        Font.loadFont(ResourceHandler.getFont("JetBrainsMono-Italic.ttf"), 16);

        // generate start page
        MenuView root = new MenuView(new CoursePage());
        Scene scene = new Scene(root, 1280, 720);
        stage.setFullScreen(true);
        stage.getIcons().add(ResourceHandler.getImage("icon.png"));

        // load CSS
        try {
            scene.getStylesheets().add(ResourceHandler.getStyleSheet("index.css"));
            scene.getStylesheets().add(ResourceHandler.getStyleSheet("input-field.css"));
            scene.getStylesheets().add(ResourceHandler.getStyleSheet("code-editor.css"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.setTitle("Learn programming with daro!");
        stage.setScene(scene);
        stage.show();
    }

}
