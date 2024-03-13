package top.ithaic.imageview;

import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.FlowPane;
import top.ithaic.shower.DiskTreeShower;

import java.io.File;

public class ImageLoadThread extends Thread{
    private TreeItem<DiskTreeShower.MyFile> selectedImage;
    private FlowPane thumbnails;
    private boolean isTerminal = false;

    public ImageLoadThread(TreeItem<DiskTreeShower.MyFile> selectedImage, FlowPane thumbnails){
        this.selectedImage = selectedImage;
        this.thumbnails = thumbnails;
    }
    @Override
    public void run(){
        File[] files = selectedImage.getValue().getFile().listFiles();
        if (files == null) return;
        for(File file : files){
            if(this.isTerminal)break;
            //如果是图片把它画出来然后添加到flowPane里面
            if(isPicture(file)){
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
    public boolean isPicture(File file){
        String[] formats = {".jpg",".jpeg",".bmp",".gif",".png"};
        boolean judge = false;
        for(String format :formats){
            if(file.getName().toLowerCase().endsWith(format))
                judge = true;
        }
        return judge;
    }
}
