package top.ithaic.shower.SlideShower;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;


public class SlideWindow {
    public SlideWindow(File[] currentFiles){
        launch(currentFiles);
    }

    public void launch(File[] currentFiles){
        FXMLLoader fxmlLoader = new FXMLLoader(SlideController.class.getResource("slideShower.fxml"));
        //传入图片即可
        new SlideFileManager(currentFiles);

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("幻灯片展示");
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }
}
