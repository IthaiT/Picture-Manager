package top.ithaic.shower.SlideShower;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import top.ithaic.Myinterface.Listener;
import top.ithaic.shower.slidePlay.SlidePlay;
import top.ithaic.utils.FilePathUtil;
import top.ithaic.utils.PictureOperationUtil;

import java.io.IOException;

public class ContextMenuListener implements Listener {
    private static ContextMenu contextMenu;
    private final MenuItem copyItem = new MenuItem("复制");
    private final MenuItem deleteItem = new MenuItem("删除");
    private final MenuItem slidePlay = new MenuItem("幻灯片放映");

    public ContextMenuListener(ContextMenu contextMenu){
        ContextMenuListener.contextMenu = contextMenu;
        ContextMenuListener.contextMenu.getItems().addAll(copyItem,deleteItem,slidePlay);
        ContextMenuListener.contextMenu.setStyle(" -fx-background-color: white");
        Listen();
    }

    @Override
    public void Listen() {
        copyItem.setOnAction(actionEvent -> {
            PictureOperationUtil.copyPictures(SlideFileManager.getCurrentIndex());
            FilePathUtil.updateFiles();
            SlideFileManager.setPictures(FilePathUtil.getCurrentFiles());
            new SlideShower().drawPicture();
        });
        deleteItem.setOnAction(actionEvent -> {
            try {
                if(PictureOperationUtil.deletePictures(SlideFileManager.getCurrentIndex())) {
                    FilePathUtil.updateFiles();
                    SlideFileManager.setPictures(FilePathUtil.getCurrentFiles());
                    if(SlideFileManager.getCurrentIndex()==SlideFileManager.getPictures().length) {
                        SlideFileManager.setCurrentIndex(SlideFileManager.getCurrentIndex() - 1);
                    }
                    SlideFileManager.setCurrentIndexProperty(SlideFileManager.getCurrentIndex());
                    SlideFileManager.setPicturesLengthProperty(SlideFileManager.getPictures().length);
                    new SlideShower().drawPicture();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        slidePlay.setOnAction((actionEvent -> {
            SlidePlay.playPicture(SlideFileManager.getPictures(),SlideFileManager.getCurrentIndex());
        }));
    }
}
