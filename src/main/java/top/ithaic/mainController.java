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
import top.ithaic.listener.*;
import top.ithaic.shower.DiskTreeShower;
import top.ithaic.shower.PathShower;
import top.ithaic.shower.PictureMessageShower;
import top.ithaic.shower.PictureShower;

public class mainController {
    @FXML
    private Button backwardButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button sortButton;
    @FXML
    private Button searchButton;
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
    private TextField searchName;



    @FXML
    private void initialize(){
        initShower();
        initListener();
    }

    //TODO 初始化各种界面节点
    private void initShower(){
        //TODO 初始化磁盘树目录
        new DiskTreeShower(disktree);
        //TODO 初始化图片预览
        new PictureShower(thumbnails);
        //TODO 初始化路径显示
        new PathShower(pathShower,anchorPane);
        //TODO 初始化图片信息统计
        new PictureMessageShower(pictureMessage);
    }

    //TODO 初始化各种监听器
    private void initListener(){
        new DiskTreeListener(disktree);
        new SliderListener(sizeChanger);
        new PathButtonListener(backwardButton, forwardButton);
        new SortButtonListener(sortButton);
        new SearchButtonListener(searchName,searchButton);
        new PictureShowerListener(thumbnails);
    }

}