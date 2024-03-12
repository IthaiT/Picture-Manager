package top.ithaic.imageview;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import top.ithaic.disktreeview.DiskTreeShower;

public class PictureShower {
    private TreeView diskTree;
    private ScrollPane pictureShower;
    private FlowPane thumbnails;
    private ImageLoadThread imageLoadThread;
    public PictureShower(ScrollPane PictureShower, FlowPane thumbnails, TreeView DiskTree){
        this.diskTree = DiskTree;
        this.pictureShower = PictureShower;
        this.thumbnails = thumbnails;
        this.addTreeListener();
    }

    private void addTreeListener(){
        this.diskTree.addEventFilter( MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            //鼠标双击选中目录树中的文件
            if(mouseEvent.getClickCount() >= 2 ){
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
                if(selectedImage!=null && selectedImage.getChildren().isEmpty()){
                    //加载图片
                    imageLoadThread = new ImageLoadThread(selectedImage,thumbnails);
                    imageLoadThread.start();
                }

            }
        });
    }
}
