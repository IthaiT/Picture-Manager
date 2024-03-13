package top.ithaic.imageview;

import javafx.application.Platform;
import javafx.scene.layout.FlowPane;
import top.ithaic.utils.PictureUtil;

import java.io.File;

public class ImageLoadThread extends Thread{
    private File selectedPath;
    private FlowPane thumbnails;
    private boolean isTerminal = false;

    public ImageLoadThread(File selectedPath, FlowPane thumbnails){
        this.selectedPath = selectedPath;
        this.thumbnails = thumbnails;
    }
    @Override
    public void run(){
        PictureUtil pictureutil = new PictureUtil();
        File[] files = selectedPath.listFiles();

        if (files == null) return;
        for(File file : files){
            if(this.isTerminal)break;
            //如果是图片把它画出来然后添加到flowPane里面
            if(pictureutil.isPicture(file)){
                Thumbnail thumbnail = new Thumbnail(file);
                Platform.runLater(()->{
                    if(this.isTerminal)return;
                    thumbnails.getChildren().add(thumbnail);
                });
            }
        }
    }

    public void terminate(){
        this.isTerminal = true;
    }

}
