package top.ithaic.imageview;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import top.ithaic.disktreeview.ShowDiskTree;

import java.io.File;

public class ShowPicture {
    private TreeView diskTree;
    private ScrollPane pictureShower;
    private FlowPane thumbnails;
    public ShowPicture(ScrollPane PictureShower, FlowPane thumbnails, TreeView DiskTree){
        this.diskTree = DiskTree;
        this.pictureShower = PictureShower;
        this.thumbnails = thumbnails;
        addListener();
    }
    private void addListener(){
        this.diskTree.addEventFilter( MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            //鼠标双击选中目录树中的文件
            if(mouseEvent.getClickCount() >= 2 ){
                TreeItem<ShowDiskTree.MyFile> selectedImage = (TreeItem<ShowDiskTree.MyFile>) diskTree.getSelectionModel().getSelectedItem();
                //处理事件
                if(selectedImage!=null && selectedImage.getChildren().isEmpty()){
                    thumbnails.getChildren().clear();
                    System.out.println("选中了"+selectedImage.getValue().toString());//测试

                    Thread imageLoadingThread = new Thread(()->{
                        //得到目录下所有文件
                        File[] pictures = selectedImage.getValue().getFile().listFiles();
                        String[] formats = {".jpg",".jpeg",".bmp",".gif",".png"};
                        if (pictures != null) {
                            //挑选出图片
                            for(File picture : pictures){
                                boolean isPicture = false;
                                for(String format :formats){
                                    if(picture.getName().toLowerCase().endsWith(format))
                                        isPicture = true;
                                }
                                //如果是图片把它画出来然后添加到flowPane里面
                                if(isPicture){
                                    System.out.println(picture);
                                    Thumbnail thumbnail = new Thumbnail(picture);
                                    Platform.runLater(()->{
                                        thumbnails.getChildren().add(thumbnail);

                                    });
                                }
                            }
                        }
                    });
                    imageLoadingThread.start();
                }

            }
        });
    }
}
