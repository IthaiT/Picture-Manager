package top.ithaic.pathview;

import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import top.ithaic.disktreeview.DiskTreeShower;


public class PathShower {
    private TreeView diskTree;
    private TextField pathShower;
    public PathShower(TreeView diskTree,TextField pathShower){
        this.diskTree = diskTree;
        this.pathShower = pathShower;
        this.pathShower.setEditable(false);
        this.addListener();
    }

    public void addListener(){
        this.diskTree.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            if(mouseEvent.getClickCount() >= 2){
                @SuppressWarnings("unchecked")
                TreeItem<DiskTreeShower.MyFile> selectedPath = (TreeItem<DiskTreeShower.MyFile>) diskTree.getSelectionModel().getSelectedItem();
                this.pathShower.setText(selectedPath.getValue().getFile().getAbsolutePath());
            }
        });
    }
}
