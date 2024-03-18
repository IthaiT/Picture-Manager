package top.ithaic.listener;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import top.ithaic.Myinterface.Listener;
import top.ithaic.utils.PictureOperationUtil;

public class PictureOperateListener implements Listener {
    private static ContextMenu contextMenuT;//On Thumbnail
    private static ContextMenu contextMenuP;//On Pane
    private static final MenuItem copyItem = new MenuItem("复制");
    private static final MenuItem renameItem = new MenuItem("重命名");
    private static final MenuItem deleteItem = new MenuItem("删除");
    private static final  MenuItem pasteItem = new MenuItem("粘贴");
    private static final MenuItem selectAllItem = new MenuItem("全选");
    public PictureOperateListener(ContextMenu contextMenuT,ContextMenu contextMenuP){
        PictureOperateListener.contextMenuT = contextMenuT;
        PictureOperateListener.contextMenuP = contextMenuP;
        contextMenuT.getItems().addAll(copyItem, renameItem, deleteItem);
        contextMenuP.getItems().addAll(pasteItem,selectAllItem);
        contextMenuT.setStyle(" -fx-background-color: white");
        contextMenuP.setStyle(" -fx-background-color: white");
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
            PictureOperationUtil.deletePictures();
        });
        pasteItem.setOnAction(actionEvent -> {
            PictureOperationUtil.pastePictures();
        });
    }
}
