package top.ithaic.imageview;

import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.FlowPane;
import top.ithaic.disktreeview.DiskTreeShower;

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
        File[] pictures = selectedImage.getValue().getFile().listFiles();
        String[] formats = {".jpg",".jpeg",".bmp",".gif",".png"};
        if (pictures != null) {
            //挑选出图片
            for(File picture : pictures){
                if(this.isTerminal)break;
                boolean isPicture = false;
                for(String format :formats){
                    if(picture.getName().toLowerCase().endsWith(format))
                        isPicture = true;
                }
                //如果是图片把它画出来然后添加到flowPane里面
                if(isPicture){
//                    System.out.println(picture);
                    Thumbnail thumbnail = new Thumbnail(picture);
                    Platform.runLater(()->{
                        if(this.isTerminal)return;
                        thumbnails.getChildren().add(thumbnail);
                    });
                }
            }
        }
    }

    public void terminate(){
        this.isTerminal = true;
    }
}
