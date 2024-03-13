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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import top.ithaic.disktreeview.DiskTreeShower;
import top.ithaic.imageview.PictureShower;
import top.ithaic.pathview.PathShower;

public class mainController {
    @FXML
    private TreeView<TreeItem<DiskTreeShower.MyFile>> disktree;
    @FXML
    private FlowPane thumbnails;
    @FXML
    private TextField pathShower;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Slider sizeChanger;

    @FXML
    private void initialize(){
        initTreeView();
        initPictureShower();
        initPathShower();
    }

    //TODO 初始化磁盘树目录
    private void initTreeView(){
        new DiskTreeShower(disktree);
    }

    //TODO 初始化图片预览
    private void initPictureShower(){
        new PictureShower(thumbnails, disktree, sizeChanger);
    }

    //TODO 当前路径
    private void initPathShower(){new PathShower(disktree,pathShower,anchorPane);}

    //TODO 改变图片大小


}