package top.ithaic.shower.SlideShower;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import top.ithaic.imageview.Thumbnail;
import java.io.IOException;
import java.util.ArrayList;

public class SlideWindow {
    public SlideWindow(ArrayList<Thumbnail> thumbnailArrayList){
        launch(thumbnailArrayList);
    }

    public void launch(ArrayList<Thumbnail> thumbnailArrayList){
        FXMLLoader fxmlLoader = new FXMLLoader(SlideController.class.getResource("slideShower.fxml"));
        //传入图片即可
        new SlideFileManager(thumbnailArrayList);

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
