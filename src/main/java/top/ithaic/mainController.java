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
import top.ithaic.listener.DiskTreeListener;
import top.ithaic.listener.SliderListener;
import top.ithaic.shower.DiskTreeShower;
import top.ithaic.shower.PictureShower;
import top.ithaic.shower.PathShower;

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
    private TextField pictureMessage;

    @FXML
    private void initialize(){
        initTreeView();
        initPictureShower();
        initPathShower();
        initListener();
    }

    //TODO 初始化磁盘树目录
    private void initTreeView(){
        new DiskTreeShower(disktree);
    }

    //TODO 初始化图片预览
    private void initPictureShower(){
        new PictureShower(thumbnails);
    }

    //TODO 当前路径
    private void initPathShower(){new PathShower(disktree,pathShower,anchorPane);}

    //TODO 初始化各种监听器
    private void initListener(){
        new DiskTreeListener(disktree);
        new SliderListener(sizeChanger);
    }
}