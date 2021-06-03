package daro.ide.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Ide extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Font.loadFont(Ide.class.getResourceAsStream("/daro/ide/fonts/Montserrat-Regular.ttf"), 16);
        Font.loadFont(Ide.class.getResourceAsStream("/daro/ide/fonts/Montserrat-Italic.ttf"), 16);
        Font.loadFont(Ide.class.getResourceAsStream("/daro/ide/fonts/Montserrat-Bold.ttf"), 16);
        Font.loadFont(Ide.class.getResourceAsStream("/daro/ide/fonts/Montserrat-BoldItalic.ttf"), 16);
        Font.loadFont(Ide.class.getResourceAsStream("/daro/ide/fonts/MaterialIcons-Regular.ttf"), 16);
        Font.loadFont(Ide.class.getResourceAsStream("/daro/ide/fonts/JetBrainsMono-Bold.ttf"), 16);
        Font.loadFont(Ide.class.getResourceAsStream("/daro/ide/fonts/JetBrainsMono-BoldItalic.ttf"), 16);
        Font.loadFont(Ide.class.getResourceAsStream("/daro/ide/fonts/JetBrainsMono-Regular.ttf"), 16);
        Font.loadFont(Ide.class.getResourceAsStream("/daro/ide/fonts/JetBrainsMono-Italic.ttf"), 16);

        Explorer explorer = new Explorer();
        Scene scene = new Scene(explorer);
        scene.getStylesheets().addAll(
            Ide.class.getResource("/daro/ide/styles/index.css").toExternalForm(),
            Ide.class.getResource("/daro/ide/styles/scrollbar.css").toExternalForm(),
            Ide.class.getResource("/daro/ide/styles/editor.css").toExternalForm(),
            Ide.class.getResource("/daro/ide/styles/terminal.css").toExternalForm(),
            Ide.class.getResource("/daro/ide/styles/syntax.css").toExternalForm()
        );

        stage.setOnCloseRequest(event -> {
            if (!explorer.allowClosing()) {
                event.consume();
            }
        });
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.setTitle("DaRo IDE");
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
    }
}

