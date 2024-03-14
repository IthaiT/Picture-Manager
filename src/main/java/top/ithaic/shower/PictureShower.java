package top.ithaic.shower;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.FlowPane;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.utils.PictureUtil;
import top.ithaic.utils.PictureUtil.*;

import java.io.File;

public class PictureShower {
    private static StringProperty currentPathProperty;
    private static File currentPath;
    private static File lastPath;
    private static FlowPane thumbnails;
    private static PictureShower.ImageLoadThread imageLoadThread;

    static {
        currentPathProperty = new SimpleStringProperty("");
        currentPath = null;
        lastPath = null;
        thumbnails = null;
    }
    public PictureShower(){}
    public PictureShower(FlowPane thumbnails){
        PictureShower.thumbnails = thumbnails;
    }

    //TODO 所有的showPicture方法到最后都要调用此方法
    public void showPicture(File[] pictures){
        currentPathProperty.setValue(currentPath.getAbsolutePath());
        //清除图片
        Platform.runLater(()->{
            thumbnails.getChildren().clear();
        });
        //若有线程，则终止
        if(imageLoadThread!=null && imageLoadThread.isAlive()){
            imageLoadThread.terminate();
        }
        //加载图片
        imageLoadThread = new ImageLoadThread(pictures);
        imageLoadThread.start();
    }

    //TODO 传入要显示的路径，显示该路径图片
    public void showPicture(File selectedPath){
        if(selectedPath==null) return;
        File[] pictures = PictureUtil.getPicturesInDirectory(selectedPath);
        if(pictures == null)return;

        lastPath = currentPath;
        currentPath = selectedPath;

        new PathShower().bindProperty();
        new PictureMessageShower().updateText();
        showPicture(pictures);
    }
    //TODO 显示当前路径图片
    public void showPicture(){
        if(currentPath!=null)this.showPicture(currentPath);
    }


    public static StringProperty getCurrentPathProperty() {
        return currentPathProperty;
    }
    public static File getCurrentPath() {
        return currentPath;
    }
    public static File getLastPath() {
        return lastPath;
    }

    private class ImageLoadThread extends Thread{
        private File[] pictures;
        private boolean isTerminal = false;
        public ImageLoadThread(File[] pictures){this.pictures = pictures;}
        @Override
        public void run(){
            for(File picture : pictures){
                if(this.isTerminal)break;
                //当数组中图片等于null时，表明到数组末尾，返回
                if(picture == null)break;
                Thumbnail thumbnail = new Thumbnail(picture);
                Platform.runLater(()->{
                    if(this.isTerminal)return;
                    thumbnails.getChildren().add(thumbnail);
                });
                }
            }
        public void terminate(){
            this.isTerminal = true;
        }
        }

}

