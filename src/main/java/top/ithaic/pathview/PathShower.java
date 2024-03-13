package top.ithaic.pathview;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import top.ithaic.disktreeview.DiskTreeShower;


public class PathShower {
    private TreeView diskTree;
    private TextField pathShower;
    private AnchorPane anchorPane;
    public PathShower(TreeView diskTree,TextField pathShower,AnchorPane father){
        this.diskTree = diskTree;
        this.pathShower = pathShower;
        this.anchorPane = father;
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
        ChangeListener<Number> anchorPaneSizeListener = ((observableValue, oldSize, newSize) -> {
            double newWidth = anchorPane.getWidth();
            this.pathShower.setPrefWidth(newWidth - 400);
        });
        this.anchorPane.widthProperty().addListener(anchorPaneSizeListener);
    }
}
