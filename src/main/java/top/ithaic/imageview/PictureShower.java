package top.ithaic.imageview;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import top.ithaic.disktreeview.DiskTreeShower;

public class PictureShower {
    private TreeView diskTree;
    private FlowPane thumbnails;
    private ImageLoadThread imageLoadThread;
    private Slider sizeChanger;
    public PictureShower( FlowPane thumbnails, TreeView DiskTree, Slider sizeChanger){
        this.diskTree = DiskTree;
        this.thumbnails = thumbnails;
        this.sizeChanger = sizeChanger;
        this.addTreeListener();
        this.addSizeListener();
    }

    //TODO 监听哪个目录被点击
    private void addTreeListener(){
        this.diskTree.addEventFilter( MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            //鼠标双击选中目录树中的文件
            if(mouseEvent.getClickCount() >= 2 ){
                showPicture();
            }
        });
    }

    //TODO 监听图片显示大小的改变
    private void addSizeListener(){
        //首先初始化Slider参数
        this.sizeChanger.setMin(60);
        this.sizeChanger.setMax(200);
        this.sizeChanger.setValue(100);
        //监听Slider的改变
        this.sizeChanger.valueProperty().addListener(((observableValue, oldValue, newValue) -> {
            showPicture();
            Thumbnail thumbnail = new Thumbnail();
            double newWidth = thumbnail.getThumbnailWidth() * ((newValue.doubleValue())/ oldValue.doubleValue());
            double newHeight = thumbnail.getThumbnailHeight() * (newValue.doubleValue() /oldValue.doubleValue());
            thumbnail.setThumbnailWidth(newWidth);
            thumbnail.setThumbnailHeight(newHeight);
        }));
    }

    //TODO 将当前目录的文件显示出来
    private void showPicture(){
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
}
