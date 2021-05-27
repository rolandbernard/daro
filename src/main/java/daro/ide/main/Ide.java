package daro.ide.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Ide extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Explorer());

        stage.setWidth(1000);
        stage.setHeight(700);
        stage.setTitle("DaRo IDE");
        stage.setScene(scene);
        stage.show();
    }
}

