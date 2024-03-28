package top.ithaic.listener;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import top.ithaic.Myinterface.Listener;
import top.ithaic.shower.SlideShower.SlideWindow;
import top.ithaic.shower.slidePlay.SlidePlay;
import top.ithaic.utils.PathUtil;
import top.ithaic.utils.PictureOperationUtil;
import top.ithaic.utils.PictureUtil;

import java.io.IOException;

public class PictureOperateListener implements Listener {
    private static final MenuItem openItem = new MenuItem("打开");
    private static final MenuItem copyItem = new MenuItem("复制");
    private static final MenuItem renameItem = new MenuItem("重命名");
    private static final MenuItem deleteItem = new MenuItem("删除");
    private static final MenuItem playHere = new MenuItem("幻灯片放映");
    private static final MenuItem pasteItem = new MenuItem("粘贴");
    private static final MenuItem selectAllItem = new MenuItem("全选");
    private static final MenuItem slidePlay = new MenuItem("播放幻灯片");

    public PictureOperateListener(ContextMenu contextMenuT, ContextMenu contextMenuP) {
        contextMenuT.getItems().addAll(openItem,copyItem, renameItem, deleteItem, playHere);
        contextMenuP.getItems().addAll(pasteItem, selectAllItem, slidePlay);
        contextMenuT.setStyle(" -fx-background-color: white");
        contextMenuP.setStyle(" -fx-background-color: white");
        Listen();
    }

    @Override
    public void Listen() {
        openItem.setOnAction(actionEvent -> {
            new SlideWindow(PathUtil.getCurrentFiles(), PictureUtil.getPictureIndex(PictureShowerListener.getThumbnailArrayList().get(0).getImageFile()));
        });
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
        playHere.setOnAction(actionEvent -> {
            SlidePlay.playPicture();
        });
        pasteItem.setOnAction(actionEvent -> {
            try {
                PictureOperationUtil.pastePictures();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        selectAllItem.setOnAction(actionEvent -> {
            PictureOperationUtil.selectAll();
        });
        slidePlay.setOnAction(actionEvent -> {
            SlidePlay.playPicture();
        });
    }
}