package top.ithaic.shower.SlideShower;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import top.ithaic.HelloApplication;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.SlideThumbnail;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.listener.PictureShowerListener;
import top.ithaic.shower.slidePlay.SlidePlay;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class SlideListener implements Listener {
    private int scannerPictureNum;
    private static Pane pane;
    private static ToolBar toolBar;
    private static BorderPane mainPane;
    private static Button slidePlay;
    private static FlowPane pictureScanner;
    private ContextMenu contextMenu = new ContextMenu();
    public SlideListener(Pane pane, Button slidePlay, ToolBar toolBar, BorderPane mainPane, FlowPane pictureScanner){
        new ContextMenuListener(contextMenu);
        SlideListener.pane = pane;
        SlideListener.slidePlay = slidePlay;
        SlideListener.toolBar = toolBar;
        SlideListener.mainPane = mainPane;
        SlideListener.pictureScanner = pictureScanner;
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
        toolBar.layoutYProperty().bind((Bindings.createDoubleBinding(()->mainPane.getHeight()-toolBar.getHeight()-80,mainPane.heightProperty())));
        //鼠标移动 显示工具栏
        mainPane.setOnMouseMoved(mouseEvent -> {
            if(mainPane.getChildren().contains(toolBar))return;
            System.out.println("显示工具栏");
            mainPane.getChildren().add(toolBar);
        });

        //下方预览图
        File[] pictures = SlideFileManager.getPictures();
        int currentIndex = SlideFileManager.getCurrentIndex();
        //初步加载8张
        for(int i=currentIndex;i<pictures.length;i++) {
            if(i-currentIndex>8)break;
            SlideThumbnail slideThumbnail = new SlideThumbnail(pictures[i]);
            pictureScanner.getChildren().add(slideThumbnail);
        }
        //按容量继续加载
        pictureScanner.widthProperty().addListener((observableValue, oldValue,newValue) -> {
            scannerPictureNum = (int)(pictureScanner.getWidth()/new SlideThumbnail().getThumbnailWidth());
            if(scannerPictureNum>pictureScanner.getChildren().size()){
                for(int i=currentIndex+pictureScanner.getChildren().size();i<currentIndex+scannerPictureNum;i++){
                    SlideThumbnail temp = new SlideThumbnail(pictures[i]);
                    pictureScanner.getChildren().add(temp);
                }
            }
            if(scannerPictureNum<pictureScanner.getChildren().size()){
                for(int i=pictureScanner.getChildren().size()-1;i>=scannerPictureNum;i--){
                    pictureScanner.getChildren().remove(i);
                }
            }
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
