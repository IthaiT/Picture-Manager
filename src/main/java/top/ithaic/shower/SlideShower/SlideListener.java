package top.ithaic.shower.SlideShower;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import top.ithaic.HelloApplication;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.SlideThumbnail;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.listener.PictureShowerListener;
import top.ithaic.listener.SliderListener;
import top.ithaic.shower.slidePlay.SlidePlay;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class SlideListener implements Listener {
    private int head;
    private int tail;
    private int scannerPictureNum;
    private static Pane pane;
    private static ToolBar toolBar;
    private static BorderPane mainPane;
    private static Button slidePlay;
    private static FlowPane pictureScanner;
    private static Button shrinkPicture;
    private static Button amplifyPicture;
    private static Button lastPicture;
    private static Button nextPicture;
    private static Pane pictureShower;
    private ContextMenu contextMenu = new ContextMenu();
    public SlideListener(Pane pane, Button slidePlay, ToolBar toolBar, BorderPane mainPane, FlowPane pictureScanner,Pane pictureShower,Button...buttons){
        new ContextMenuListener(contextMenu);
        SlideListener.pane = pane;
        SlideListener.slidePlay = slidePlay;
        SlideListener.toolBar = toolBar;
        SlideListener.mainPane = mainPane;
        SlideListener.pictureScanner = pictureScanner;
        SlideListener.lastPicture = buttons[0];
        SlideListener.nextPicture = buttons[1];
        SlideListener.amplifyPicture = buttons[2];
        SlideListener.shrinkPicture = buttons[3];
        SlideListener.pictureShower = pictureShower;
        pictureScanner.setHgap(10);
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
        //工具栏坐标位置监听
        toolBar.layoutXProperty().bind(Bindings.createDoubleBinding(()->mainPane.getWidth()/2-toolBar.getWidth()/2,mainPane.widthProperty()));
        toolBar.layoutYProperty().bind((Bindings.createDoubleBinding(()->mainPane.getHeight()-toolBar.getHeight()-80,mainPane.heightProperty())));
        //鼠标移动 显示工具栏
        mainPane.setOnMouseMoved(mouseEvent -> {
            if(mainPane.getChildren().contains(toolBar))return;
            mainPane.getChildren().add(toolBar);
        });

        //下方预览图
        File[] pictures = SlideFileManager.getPictures();
        int currentIndex = SlideFileManager.getCurrentIndex();

        pictureLoad(currentIndex,pictures);
        indexListen(pictures);
        buttonListen();
    }

    //此定时 隐藏工具栏
    private void startTimer(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    if(!mainPane.getChildren().contains(toolBar))return;
                    mainPane.getChildren().remove(toolBar);
                });
            }
        };
        timer.schedule(task,0,5000);
    }

    private void pictureLoad(int currentIndex,File[] pictures){
        head = currentIndex-1;
        tail = currentIndex+1;
        //初步加载
        scanPicture(currentIndex,pictures);
        //按容量继续加载
        pictureScanner.widthProperty().addListener((observableValue, oldValue,newValue) -> {
            scannerPictureNum = (int)(pictureScanner.getWidth()/(new SlideThumbnail().getThumbnailWidth()+20));
            int numGap = scannerPictureNum - pictureScanner.getChildren().size();
            while(numGap > 0){
                for(int i=tail;i<tail+numGap&&i<pictures.length;i++){
                    SlideThumbnail temp = new SlideThumbnail(pictures[i]);
                    pictureScanner.getChildren().add(temp);
                    tail++;
                    numGap--;
                }
                for(int i=head;i>head-numGap&&i>0;i--){
                    SlideThumbnail temp = new SlideThumbnail(pictures[i]);
                    pictureScanner.getChildren().add(0,temp);
                    head--;
                    numGap--;
                }
            }
            if(numGap < 0){
                for(int i=pictureScanner.getChildren().size()-1;i>=scannerPictureNum&&i>=0;i--){
                    pictureScanner.getChildren().remove(i);
                    tail--;
                    numGap++;
                }
            }
        });
    }

    private void indexListen(File[] pictures){
        SlideFileManager.currentIndexPropertyProperty().addListener(((observableValue, oldValue,newValue) -> {
            if(newValue.intValue() < oldValue.intValue()){
                ((SlideThumbnail) pictureScanner.getChildren().get(SlideFileManager.getCurrentIndex() - head)).setUnSelectedStyle();
                if(head>=0) {
                    pictureScanner.getChildren().add(0, new SlideThumbnail(pictures[head]));
                    head--;
                    pictureScanner.getChildren().remove(pictureScanner.getChildren().size() - 1);
                    tail--;
                }
                ((SlideThumbnail) pictureScanner.getChildren().get(SlideFileManager.getCurrentIndex() - head - 1)).setSelectedStyle();
            }
            if(newValue.intValue() > oldValue.intValue()) {
                ((SlideThumbnail) pictureScanner.getChildren().get(SlideFileManager.getCurrentIndex() - head - 2)).setUnSelectedStyle();
                if(tail<pictures.length) {
                    pictureScanner.getChildren().remove(0);
                    head++;
                    tail++;
                    pictureScanner.getChildren().add(new SlideThumbnail(pictures[tail - 1]));
                }
                ((SlideThumbnail) pictureScanner.getChildren().get(SlideFileManager.getCurrentIndex() - head - 1)).setSelectedStyle();
            }
        }));
    }

    private void buttonListen(){
        //按钮监听
        slidePlay.setOnMouseClicked(mouseEvent -> {
            SlidePlay.playPicture(SlideFileManager.getPictures(),SlideFileManager.getCurrentIndex());
        });

        lastPicture.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            new SlideShower().lastPicture();
        });

        nextPicture.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            new SlideShower().nextPicture();

        });
        pictureShower.getParent().addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.LEFT) {
                new SlideShower().lastPicture();
            }
            if (keyEvent.getCode() == KeyCode.RIGHT) {
                new SlideShower().nextPicture();
            }
        });
        amplifyPicture.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> new SlideShower().amplifyPicture());
        shrinkPicture.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> new SlideShower().shrinkPicture());
    }

    private void scanPicture(int currentIndex,File[] pictures){
        SlideThumbnail first = new SlideThumbnail(pictures[currentIndex]);
        first.setSelectedStyle();
        pictureScanner.getChildren().add(first);
        while(tail-head<8&&head>0&&tail<pictures.length){
            if(tail-head<8&&head>0) {
                SlideThumbnail slideThumbnail = new SlideThumbnail(pictures[head]);
                pictureScanner.getChildren().add(0,slideThumbnail);
                head--;
            }
            if(tail-head<8&&tail<pictures.length){
                System.out.println(tail);
                SlideThumbnail slideThumbnail = new SlideThumbnail(pictures[tail]);
                pictureScanner.getChildren().add(slideThumbnail);
                tail++;
            }
        }
    }

}
