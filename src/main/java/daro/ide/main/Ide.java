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


        Scene scene = new Scene(new Explorer());
        scene.getStylesheets().add(Ide.class.getResource("/daro/ide/styles/index.css").toExternalForm());
        scene.getStylesheets().add(Ide.class.getResource("/daro/ide/styles/editor.css").toExternalForm());
        scene.getStylesheets().add(Ide.class.getResource("/daro/ide/styles/terminal.css").toExternalForm());
        scene.getStylesheets().add(Ide.class.getResource("/daro/ide/styles/syntax.css").toExternalForm());

        stage.setWidth(1000);
        stage.setHeight(700);
        stage.setTitle("DaRo IDE");
        stage.setScene(scene);
        stage.show();
    }
}

