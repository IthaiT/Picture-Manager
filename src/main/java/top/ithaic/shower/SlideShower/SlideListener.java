package top.ithaic.shower.SlideShower;

import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import top.ithaic.HelloApplication;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.listener.PictureShowerListener;

public class SlideListener implements Listener {
    private static Pane pane;
    private ContextMenu contextMenu = new ContextMenu();
    public SlideListener(Pane pane){
        new ContextMenuListener(contextMenu);
        SlideListener.pane = pane;
        Listen();
    }
    @Override
    public void Listen() {
        pane.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.SECONDARY){
                contextMenu.show(pane,mouseEvent.getScreenX(),mouseEvent.getScreenY());
            }
        });
    }
}