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

public class mainController {
    @FXML
    private TreeView disktree;

    @FXML
    private void initialize(){
        initTreeView();
    }

    //TODO 初始化磁盘树目录
    private void initTreeView(){
        new ShowDiskTree(disktree);
    }

}