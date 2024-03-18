package top.ithaic.shower.SlideShower;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import top.ithaic.Myinterface.Listener;
import top.ithaic.utils.PictureOperationUtil;

import java.io.IOException;

public class ContextMenuListener implements Listener {
    private static ContextMenu contextMenu;
    private static final MenuItem copyItem = new MenuItem("复制");
    private static final MenuItem renameItem = new MenuItem("重命名");
    private static final MenuItem deleteItem = new MenuItem("删除");

    public ContextMenuListener(ContextMenu contextMenu){
        ContextMenuListener.contextMenu = contextMenu;
        ContextMenuListener.contextMenu.getItems().addAll(copyItem,renameItem,deleteItem);
        ContextMenuListener.contextMenu.setStyle(" -fx-background-color: white");
        Listen();
    }

    @Override
    public void Listen() {
        copyItem.setOnAction(actionEvent -> {
            PictureOperationUtil.copyPictures();
        });
        renameItem.setOnAction(actionEvent -> {
            PictureOperationUtil.renamePictures();
        });
        deleteItem.setOnAction(actionEvent -> {
            try {
                PictureOperationUtil.deletePictures();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
