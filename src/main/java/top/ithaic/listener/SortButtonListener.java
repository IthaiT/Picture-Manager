package top.ithaic.listener;

import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import top.ithaic.utils.PictureSorterUtil;
import top.ithaic.utils.PictureUtil;

import java.io.IOException;

public class SortButtonListener {
    private static Button sortButton;
    private static ContextMenu contextMenu;
    private static CheckMenuItem sortWithName;
    private static CheckMenuItem sortWithSize;
    private static CheckMenuItem sortWithLastModify;
    public SortButtonListener(Button sortButton){
        SortButtonListener.sortButton = sortButton;
        //初始化button样式
        contextMenu = new ContextMenu();
        sortWithName = new CheckMenuItem("文件名称");
        sortWithSize = new CheckMenuItem("文件大小");
        sortWithLastModify = new CheckMenuItem("修改时间");
        contextMenu.getItems().addAll(sortWithName,sortWithSize,sortWithLastModify);
        sortButton.setContextMenu(contextMenu);
        addListener();
    }

    private void addListener(){
        SortButtonListener.sortButton.setOnMouseClicked(mouseEvent->{
            if(mouseEvent.getClickCount() >= 1){
                //contextMenu显示在sortButton下面
                sortButton.getContextMenu().show(sortButton, sortButton.localToScreen(sortButton.getBoundsInLocal()).getMinX(),
                        sortButton.localToScreen(sortButton.getBoundsInLocal()).getMaxY());
            }
        });
        //实现三个按钮互斥效果并且相应点击
        sortWithName.setOnAction(actionEvent -> {
            if(sortWithName.isSelected()){
                sortWithSize.setSelected(false);
                sortWithLastModify.setSelected(false);
                PictureSorterUtil.sortWithName();
            }
        });
        sortWithSize.setOnAction(actionEvent -> {
            if(sortWithSize.isSelected()){
                sortWithName.setSelected(false);
                sortWithLastModify.setSelected(false);
                PictureSorterUtil.sortWithSize();
            }
        });
        sortWithLastModify.setOnAction(actionEvent -> {
            if(sortWithLastModify.isSelected()){
                sortWithName.setSelected(false);
                sortWithSize.setSelected(false);
                PictureSorterUtil.sortWithLastModify();
            }
        });
    }
}
