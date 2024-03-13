package top.ithaic.shower;

import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;
import top.ithaic.imageview.ImageLoadThread;

public class PictureShower {
    private static FlowPane thumbnails;
    private static TreeView diskTree;
    private ImageLoadThread imageLoadThread;

    public PictureShower(){}
    public PictureShower( FlowPane thumbnails){
        PictureShower.thumbnails = thumbnails;
    }

    public PictureShower(TreeView diskTree){
        PictureShower.diskTree = diskTree;
    }

    //TODO 将当前目录的文件显示出来
    public void showPicture(){
        @SuppressWarnings("unchecked")
        TreeItem<DiskTreeShower.MyFile> selectedImage = (TreeItem<DiskTreeShower.MyFile>) diskTree.getSelectionModel().getSelectedItem();
        //清除图片
        Platform.runLater(()->{
            thumbnails.getChildren().clear();
        });
        //若有线程，则终止
        if(imageLoadThread!=null && imageLoadThread.isAlive()){
            imageLoadThread.terminate();
        }
        //处理事件
        if(selectedImage!=null){
            //加载图片
            imageLoadThread = new ImageLoadThread(selectedImage,thumbnails);
            imageLoadThread.start();
        }
    }
}
