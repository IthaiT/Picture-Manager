package top.ithaic.shower.SlideShower;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SlideWindow {
    public SlideWindow(File picture){
        launch(picture);
    }

    public void launch(File picture){
        FXMLLoader fxmlLoader = new FXMLLoader(SlideController.class.getResource("slideShower.fxml"));
        new SlideFileManager(picture);
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle(picture.getName());
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }
}
