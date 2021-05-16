package daro.ide.main;

import java.nio.file.Paths;

import daro.ide.files.FileTree;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class IDE extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group(new FileTree(Paths.get(System.getProperty("user.home"))));
        Scene scene = new Scene(root);

        stage.setWidth(1000);
        stage.setHeight(700);
        stage.setTitle("DaRo IDE");
        stage.setScene(scene);
        stage.show();
    }
}

