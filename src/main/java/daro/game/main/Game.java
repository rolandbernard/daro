package daro.game.main;

import daro.game.io.PathHandler;
import daro.game.pages.CoursePage;
import daro.game.views.MenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.Map;

public class Game extends Application {
    public static final double HEIGHT = 720;
    public static final double WIDTH = 1280;
    public static final Map<String, String> colorTheme = new HashMap<>();

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

        initColorScheme();

        // generate start page
        MenuView root = new MenuView(new CoursePage());
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setFullScreen(true);
        stage.getIcons().add(PathHandler.getImage("icon.png"));

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

    private void initColorScheme() {
        colorTheme.put("accent", "#FF3D23");
        colorTheme.put("background", "#261262");
        colorTheme.put("lightBackground", "#381A90");
        colorTheme.put("darkBackground", "#1D1044");
    }
}
