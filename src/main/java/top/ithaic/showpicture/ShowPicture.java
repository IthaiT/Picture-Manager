package top.ithaic.showpicture;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.util.concurrent.Flow;

public class ShowPicture {
    private TreeView DiskTree;
    private FlowPane PictureShower;
    public ShowPicture(FlowPane PictureShower,TreeView DiskTree){
        this.DiskTree = DiskTree;
        this.PictureShower = PictureShower;
        addListener();
    }
    private void addListener(){
        this.DiskTree.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            //鼠标双击选中目录树中的文件
            if(mouseEvent.getClickCount() >= 2 ){
                TreeItem<ShowDiskTree.MyFile> selectedImage = (TreeItem<ShowDiskTree.MyFile>)DiskTree.getSelectionModel().getSelectedItem();
                //处理事件
                if(selectedImage!=null && selectedImage.getChildren().isEmpty())
                    System.out.println("选中了"+selectedImage.getValue().toString());
            }
        });
    }
}
