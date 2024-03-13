package top.ithaic.listener;

import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import top.ithaic.shower.PictureShower;

public class DiskTreeListener {
    private static TreeView diskTree;
     public DiskTreeListener(TreeView diskTree){
         DiskTreeListener.diskTree = diskTree;
         addDiskTreeListener();
    }
    private void addDiskTreeListener(){
        PictureShower pictureShower = new PictureShower(diskTree);
        diskTree.addEventFilter( MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            //鼠标双击选中目录树中的文件
            if(mouseEvent.getClickCount() >= 2 ){
                pictureShower.showPicture();
            }
        });
    }
}
