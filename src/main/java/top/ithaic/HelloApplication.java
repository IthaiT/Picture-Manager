package top.ithaic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import top.ithaic.utils.StageManager;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainview.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Image icon = new Image(String.valueOf(HelloApplication.class.getResource("softicon.png")));
        scene.getStylesheets().add(String.valueOf(HelloApplication.class.getResource("pictureshower.css")));
        stage.getIcons().add(icon);
        stage.setTitle("ithaic");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(500);
        StageManager.pushStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        System.exit(0);
    }
}