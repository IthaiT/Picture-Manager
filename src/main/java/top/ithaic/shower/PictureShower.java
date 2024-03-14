package top.ithaic.shower;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.FlowPane;
import top.ithaic.imageview.ImageLoadThread;

import java.io.File;

public class PictureShower {
    private static StringProperty currentPathProperty;
    private static File currentPath;
    private static File lastPath;
    private static FlowPane thumbnails;
    private static ImageLoadThread imageLoadThread;

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

    //TODO 将当前目录的文件显示出来
    public void showPicture(File selectedPath){
        if(selectedPath==null) return;

        lastPath = currentPath;
        currentPath = selectedPath;

        new PathShower().bindProperty();
        new PictureMessageShower().updateText();

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
        imageLoadThread = new ImageLoadThread(selectedPath,thumbnails);
        imageLoadThread.start();

    }
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
}
