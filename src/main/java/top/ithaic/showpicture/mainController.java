/*
*  主界面采用borderPane
*  top menuBar
*  left treeView
*  center FlowPane
*  right null
*  bottom textFiled
* */



package top.ithaic.showpicture;

import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;

public class mainController {
    @FXML
    private TreeView disktree;
    @FXML
    private FlowPane PictureShower;

    @FXML
    private void initialize(){
        initTreeView();
        initPictureShower();
    }

    //TODO 初始化磁盘树目录
    private void initTreeView(){
        new ShowDiskTree(disktree);
    }

    private void initPictureShower(){
        new ShowPicture(PictureShower,disktree);
    }

}