/*
*  主界面采用borderPane
*  top menuBar
*  left treeView
*  center FlowPane
*  right null
*  bottom textFiled
* */



package top.ithaic;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;
import top.ithaic.disktreeview.ShowDiskTree;
import top.ithaic.imageview.ShowPicture;

public class mainController {
    @FXML
    private TreeView disktree;
    @FXML
    private ScrollPane pictureShower;
    @FXML
    private FlowPane thumbnails;

    @FXML
    private void initialize(){
        initTreeView();
        initPictureShower();
    }

    //TODO 初始化磁盘树目录
    private void initTreeView(){
        new ShowDiskTree(disktree);
    }

    //TODO 初始化图片预览
    private void initPictureShower(){
        new ShowPicture(pictureShower,thumbnails, disktree);
    }

}