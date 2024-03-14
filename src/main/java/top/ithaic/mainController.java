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
import top.ithaic.listener.ButtonListener;
import top.ithaic.listener.DiskTreeListener;
import top.ithaic.listener.SliderListener;
import top.ithaic.listener.SortButtonListener;
import top.ithaic.shower.*;
import top.ithaic.utils.PictureSorterUtil;

public class mainController {
    @FXML
    private Button backwardButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button sortButton;
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
        initDiskTreeShower();
        initPictureShower();
        initPathShower();
        initPictureMessage();
        initListener();
    }

    //TODO 初始化磁盘树目录
    private void initDiskTreeShower(){
        new DiskTreeShower(disktree);
    }

    //TODO 初始化图片预览
    private void initPictureShower(){
        new PictureShower(thumbnails);
    }

    //TODO 初始化路径显示
    private void initPathShower(){new PathShower(pathShower,anchorPane);}

    //TODO 初始化图片信息统计
    private void initPictureMessage(){
        new PictureMessageShower(pictureMessage);
    }
    //TODO 初始化各种监听器
    private void initListener(){
        new DiskTreeListener(disktree);
        new SliderListener(sizeChanger);
        new ButtonListener(backwardButton, forwardButton);
        new SortButtonListener(sortButton);
    }

}