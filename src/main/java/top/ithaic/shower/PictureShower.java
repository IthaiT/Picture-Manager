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

    //TODO 所有的showPicture方法到最后都要调用此方法
    public void showPicture(File[] pictures){
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

    //TODO 传入要显示的路径，显示该路径图片
    public void showPicture(File selectedPath){
        if(selectedPath==null) return;
        File[] pictures = PictureUtil.getPicturesInDirectory(selectedPath);
        if(pictures == null)return;
        //维护路径信息
        PathUtil.updatePath(selectedPath);
        showPicture(pictures);
    }
    //TODO 显示当前路径图片
    public void showPicture(){
        if(PathUtil.getCurrentPath() !=null)this.showPicture(PathUtil.getCurrentPath());
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

