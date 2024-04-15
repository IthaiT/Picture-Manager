package top.ithaic.shower.SlideShower;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;

import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import javafx.scene.layout.Pane;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.SlideThumbnail;
import top.ithaic.shower.slidePlay.SlidePlay;
import top.ithaic.utils.PictureUtil;

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
    private static Button leftRotatePicture;
    private static Button rightRotatePicture;
    private static Pane pictureShower;
    private static Button compressImage;
    private static Pane compressPane;
    private ContextMenu contextMenu = new ContextMenu();
    public SlideListener(Pane pane, Button slidePlay, ToolBar toolBar, BorderPane mainPane, FlowPane pictureScanner,Pane pictureShower,Pane compressPane,Button...buttons){
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
        SlideListener.leftRotatePicture = buttons[4];
        SlideListener.rightRotatePicture = buttons[5];
        SlideListener.compressImage = buttons[6];
        SlideListener.pictureShower = pictureShower;
        SlideListener.compressPane = compressPane;
        pictureScanner.setHgap(10);
        Listen();
        SlideListener.pictureShower.getChildren().remove(compressPane);
    }
    @Override
    public void Listen() {
        //工具栏坐标位置监听
        toolBar.layoutXProperty().bind(Bindings.createDoubleBinding(()->mainPane.getWidth()/2-toolBar.getWidth()/2,mainPane.widthProperty()));
        toolBar.layoutYProperty().bind((Bindings.createDoubleBinding(()->mainPane.getHeight()-toolBar.getHeight()-80,mainPane.heightProperty())));
        //压缩参数窗口位置监听
        compressPane.layoutXProperty().bind(Bindings.createDoubleBinding(()->(pictureShower.getWidth()-compressPane.getPrefWidth())/2,pictureShower.widthProperty()));
        compressPane.layoutYProperty().bind(Bindings.createDoubleBinding(()->(pictureShower.getHeight()-compressPane.getPrefHeight())/2,pictureShower.heightProperty()));
        //定时检测工具栏
        startTimer();
        //监听所需参数
        File[] pictures = SlideFileManager.getPictures();
        int currentIndex = SlideFileManager.getCurrentIndex();
        //其他监听
        pictureLoad(currentIndex,pictures);
        indexListen(pictures);
        buttonListen();
        mouseListen();
    }

    //此定时 隐藏工具栏
    private void startTimer(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    if(!mainPane.getChildren().contains(toolBar))return;
                    pictureShower.getParent().requestFocus(); //当ToolBar被移除时，焦点转移到BoardPane上，确保键盘监听正常
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
            if(numGap > 0){
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
                ((SlideThumbnail) pictureScanner.getChildren().get(oldValue.intValue() - head - 1)).setUnSelectedStyle();
                for(int i=0;i<oldValue.intValue()-newValue.intValue()&&head>=0;i++) {
                    pictureScanner.getChildren().add(0, new SlideThumbnail(pictures[head]));
                    head--;
                    pictureScanner.getChildren().remove(pictureScanner.getChildren().size() - 1);
                    tail--;
                }
                ((SlideThumbnail) pictureScanner.getChildren().get(newValue.intValue() - head - 1)).setSelectedStyle();
            }
            if(newValue.intValue() > oldValue.intValue()) {
                ((SlideThumbnail) pictureScanner.getChildren().get(oldValue.intValue() - head - 1)).setUnSelectedStyle();
                for(int i=0;i<newValue.intValue()-oldValue.intValue()&&tail<pictures.length;i++) {
                    pictureScanner.getChildren().remove(0);
                    head++;
                    tail++;
                    pictureScanner.getChildren().add(new SlideThumbnail(pictures[tail - 1]));
                }
                ((SlideThumbnail) pictureScanner.getChildren().get(newValue.intValue() - head - 1)).setSelectedStyle();
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
        leftRotatePicture.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> new SlideShower().rotatePicture(-90));
        rightRotatePicture.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> new SlideShower().rotatePicture(90));
        compressImage.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            if(!pictureShower.getChildren().contains(compressPane)) {
                pictureShower.getChildren().add(compressPane);
                compressPane.getStyleClass().add("compressPane");
            }
            else pictureShower.getChildren().remove(compressPane);

        });
    }

    private void mouseListen(){
        //主视图点击
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

        //鼠标移动 显示工具栏
        mainPane.setOnMouseMoved(mouseEvent -> {
            if(mainPane.getChildren().contains(toolBar))return;
            mainPane.getChildren().add(toolBar);
        });

        //预览图点击
        pictureScanner.setOnMouseClicked(mouseEvent -> {
            for(Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
                if (node instanceof SlideThumbnail && node.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY())) {
                    int tempIndex = PictureUtil.getPictureIndex(((SlideThumbnail) node).getImageFile());
                    SlideFileManager.setCurrentIndex(tempIndex);
                    SlideFileManager.setCurrentIndexProperty(tempIndex);
                    SlideShower.recoverPicture();
                    new SlideShower().drawPicture();
                    break;
                }
            }
        });
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
