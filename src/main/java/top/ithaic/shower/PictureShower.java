package top.ithaic.shower;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.listener.PictureShowerListener;
import top.ithaic.utils.PathUtil;
import top.ithaic.utils.PictureUtil;

import java.beans.EventHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;

public class PictureShower {
    private static FlowPane thumbnails;
    private static ScrollPane scrollPane;
    private static PictureShower.ImageLoadThread imageLoadThread;
    public PictureShower(){}
    public PictureShower(FlowPane thumbnails,ScrollPane scrollPane){
        PictureShower.thumbnails = thumbnails;
        PictureShower.scrollPane = scrollPane;
    }
    public static FlowPane getThumbnails() {
        return thumbnails;
    }

    //TODO 传入File[]数组显示图片
    public void showPicture(File[] pictures){
        if(pictures == null)return;
        //维护工具类属性
        PathUtil.updateFiles(pictures);
        //维护属性绑定
        new PathShower().bindProperty();
        new PictureMessageShower().updateText();
        //清除图片
        Platform.runLater(()->{
            thumbnails.getChildren().clear();
            if(pictures.length == 0){
                scrollPane.setContent(PictureShowerListener.getNoPicturePane());
            }
            else{
                scrollPane.setContent(thumbnails);
            }
        });

        //若有线程，则终止
        if(imageLoadThread!=null && imageLoadThread.isAlive()){
            imageLoadThread.terminate();
        }
        //加载图片
        imageLoadThread = new ImageLoadThread(pictures);
        imageLoadThread.start();
    }

    //TODO 传入路径显示图片
    public void showPicture(File selectedPath){
        if(selectedPath==null) return;;
        File[] pictures = PictureUtil.getPicturesInDirectory(selectedPath);
        //维护路径信息
        PathUtil.updatePath(selectedPath);
        showPicture(pictures);
    }
    //TODO 无参显示当前图片
    public void showPicture(){
        //维护工具类属性
        PathUtil.updateFiles();
        if(PathUtil.getCurrentFiles()!=null)
            this.showPicture(PathUtil.getCurrentFiles());
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
            Platform.runLater(()->{
                if(this.isTerminal)return;
                PictureShowerListener.getThumbnailArrayList().clear();
            });
        }
        public void terminate(){
            this.isTerminal = true;
        }
        }

}

