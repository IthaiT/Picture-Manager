package top.ithaic.listener;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import top.ithaic.Myinterface.Listener;
import top.ithaic.shower.DiskTreeShower;
import top.ithaic.shower.PictureShower;

import java.io.File;

public class DiskTreeListener implements Listener {
    private static TreeView diskTree;
     public DiskTreeListener(TreeView diskTree){
         DiskTreeListener.diskTree = diskTree;
         Listen();
    }
    @Override
    public void Listen(){
        diskTree.addEventFilter( MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            //鼠标双击选中目录树中的文件
            @SuppressWarnings("unchecked")
            TreeItem<DiskTreeShower.MyFile> selectedItem = (TreeItem<DiskTreeShower.MyFile>) diskTree.getSelectionModel().getSelectedItem();
            if(selectedItem == null) return;
            File selectedPath = selectedItem.getValue().getFile();
            if(mouseEvent.getClickCount() >= 2 ){
                new PictureShower().showPicture(selectedPath);
            }
        });
    }
}
