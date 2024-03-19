package top.ithaic.shower;

import javafx.application.Platform;
import javafx.scene.layout.FlowPane;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.utils.PathUtil;
import top.ithaic.utils.PictureUtil;

import java.io.File;

public class PictureShower {
    private static FlowPane thumbnails;
    private static PictureShower.ImageLoadThread imageLoadThread;

    public PictureShower(){}
    public PictureShower(FlowPane thumbnails){
        PictureShower.thumbnails = thumbnails;
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
        if(PathUtil.getCurrentFiles()!=null)this.showPicture(PathUtil.getCurrentFiles());
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

