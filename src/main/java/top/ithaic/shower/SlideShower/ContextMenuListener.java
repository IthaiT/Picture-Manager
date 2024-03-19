package top.ithaic.shower.SlideShower;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import top.ithaic.Myinterface.Listener;
import top.ithaic.shower.PictureShower;
import top.ithaic.shower.slidePlay.SlidePlay;
import top.ithaic.utils.PathUtil;
import top.ithaic.utils.PictureOperationUtil;

import java.io.IOException;

public class ContextMenuListener implements Listener {
    private static ContextMenu contextMenu;
    private final MenuItem copyItem = new MenuItem("复制");
    private final MenuItem renameItem = new MenuItem("重命名");
    private final MenuItem deleteItem = new MenuItem("删除");
    private final MenuItem slidePlay = new MenuItem("幻灯片放映");

    public ContextMenuListener(ContextMenu contextMenu){
        ContextMenuListener.contextMenu = contextMenu;
        ContextMenuListener.contextMenu.getItems().addAll(copyItem,renameItem,deleteItem,slidePlay);
        ContextMenuListener.contextMenu.setStyle(" -fx-background-color: white");
        Listen();
    }

    @Override
    public void Listen() {
        copyItem.setOnAction(actionEvent -> {
            PictureOperationUtil.copyPictures(SlideFileManager.getCurrentIndex());
            PathUtil.updateFiles();
            SlideFileManager.setPictures(PathUtil.getCurrentFiles());
            new SlideShower().drawPicture();
        });
        renameItem.setOnAction(actionEvent -> {
            PictureOperationUtil.renamePictures(SlideFileManager.getCurrentIndex());
            PathUtil.updateFiles();
            SlideFileManager.setPictures(PathUtil.getCurrentFiles());
            new SlideShower().drawPicture();
        });
        deleteItem.setOnAction(actionEvent -> {
            try {
                PictureOperationUtil.deletePictures(SlideFileManager.getCurrentIndex());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            PathUtil.updateFiles();
            SlideFileManager.setPictures(PathUtil.getCurrentFiles());
            SlideFileManager.setCurrentIndex((SlideFileManager.getCurrentIndex()-1)>=0?SlideFileManager.getCurrentIndex()-1:SlideFileManager.getCurrentIndex());
            new SlideShower().drawPicture();
        });
        slidePlay.setOnAction((actionEvent -> {
            SlidePlay.playPicture(SlideFileManager.getPictures(),SlideFileManager.getCurrentIndex());
        }));
    }
}
