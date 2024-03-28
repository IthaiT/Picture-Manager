package top.ithaic.shower.SlideShower;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import top.ithaic.HelloApplication;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.listener.PictureShowerListener;
import top.ithaic.shower.slidePlay.SlidePlay;

import java.util.Timer;
import java.util.TimerTask;

public class SlideListener implements Listener {
    private static Pane pane;
    private static ToolBar toolBar;
    private static BorderPane mainPane;
    private static Button slidePlay;
    private ContextMenu contextMenu = new ContextMenu();
    public SlideListener(Pane pane, Button slidePlay, ToolBar toolBar, BorderPane mainPane){
        new ContextMenuListener(contextMenu);
        SlideListener.pane = pane;
        SlideListener.slidePlay = slidePlay;
        SlideListener.toolBar = toolBar;
        SlideListener.mainPane = mainPane;
        Listen();
        startTimer();
    }
    @Override
    public void Listen() {
        pane.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.SECONDARY){
                contextMenu.show(pane,mouseEvent.getScreenX(),mouseEvent.getScreenY());
            }
            if(mouseEvent.getButton() == MouseButton.PRIMARY){
                if(mouseEvent.getClickCount()>=2)
                    SlidePlay.playPicture(SlideFileManager.getPictures(),SlideFileManager.getCurrentIndex());
                contextMenu.hide();
            }
        });

        slidePlay.setOnMouseClicked(mouseEvent -> {
            SlidePlay.playPicture(SlideFileManager.getPictures(),SlideFileManager.getCurrentIndex());
        });
        //工具栏坐标位置监听
        toolBar.layoutXProperty().bind(Bindings.createDoubleBinding(()->mainPane.getWidth()/2-toolBar.getWidth()/2,mainPane.widthProperty()));
        toolBar.layoutYProperty().bind((Bindings.createDoubleBinding(()->mainPane.getHeight()-toolBar.getHeight()-50,mainPane.heightProperty())));
        //鼠标移动 显示工具栏
        mainPane.setOnMouseMoved(mouseEvent -> {
            if(mainPane.getChildren().contains(toolBar))return;
            System.out.println("显示工具栏");
            mainPane.getChildren().add(toolBar);
        });
    }

    //此定时 隐藏工具栏
    private void startTimer(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    if(!mainPane.getChildren().contains(toolBar))return;
                    System.out.println("隐藏工具栏");
                    mainPane.getChildren().remove(toolBar);
                });
            }
        };
        timer.schedule(task,0,5000);
    }
}
