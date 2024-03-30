package top.ithaic.shower.SlideShower;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import top.ithaic.HelloApplication;
import top.ithaic.listener.PictureShowerListener;
import top.ithaic.utils.StageManager;

import java.io.File;
import java.io.IOException;


public class SlideWindow {
    public SlideWindow(File[] currentFiles,int currentIndex){
        launch(currentFiles,currentIndex);
    }

    public void launch(File[] currentFiles,int currentIndex){
        FXMLLoader fxmlLoader = new FXMLLoader(SlideController.class.getResource("slideShower.fxml"));
        //幻灯片窗口文件管理
        new SlideFileManager(currentFiles,currentIndex);

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scene.getStylesheets().add(String.valueOf(HelloApplication.class.getResource("slideStyle.css")));
        Stage stage = new Stage();
        stage.setTitle("幻灯片展示");
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        StageManager.pushStage(stage);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            StageManager.popStage();
            PictureShowerListener.setSlideWindow(null);
        });
    }
}
