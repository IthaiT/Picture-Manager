package top.ithaic.shower.SlideShower;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;

import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.*;

public class SlideListener implements Listener {
    boolean addFlag = true;  //标志位 false为在头部添加图片 否则在尾部添加图片
    boolean delFlag = true;  //标志位 false为在头部删除图片 否则在尾部删除图片
    private boolean isMouseInToolBar;
    private static int head;
    private static int tail;
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
        isMouseInToolBar = false;
        Listen();
        setIcon();
        SlideListener.pictureShower.getChildren().remove(compressPane);
    }
    @Override
    public void Listen() {
        //工具栏坐标位置监听
        toolBar.layoutXProperty().bind(Bindings.createDoubleBinding(()->mainPane.getWidth()/2-toolBar.getWidth()/2,mainPane.widthProperty()));
        toolBar.layoutYProperty().bind((Bindings.createDoubleBinding(()->mainPane.getHeight()-toolBar.getHeight()-80,mainPane.heightProperty())));
        toolBar.addEventFilter(MouseEvent.MOUSE_ENTERED,mouseEvent -> isMouseInToolBar = true);
        toolBar.addEventFilter(MouseEvent.MOUSE_EXITED,mouseEvent -> isMouseInToolBar = false);
        //压缩参数窗口位置监听
        compressPane.layoutXProperty().bind(Bindings.createDoubleBinding(()->(pictureShower.getWidth()-compressPane.getPrefWidth())/2,pictureShower.widthProperty()));
        compressPane.layoutYProperty().bind(Bindings.createDoubleBinding(()->(pictureShower.getHeight()-compressPane.getPrefHeight())/2,pictureShower.heightProperty()));
        //定时检测工具栏
        startTimer();
        //其他监听
        pictureLoad();
        lengthListen();
        indexListen();
        buttonListen();
        mouseListen();


        //按容量继续加载
        pictureScanner.widthProperty().addListener((observableValue, oldValue,newValue) -> {
            File[] pictures = SlideFileManager.getPictures();
            int currentIndex = SlideFileManager.getCurrentIndex();
            scannerPictureNum = (int)(pictureScanner.getWidth()/(new SlideThumbnail().getThumbnailWidth()+20));
            int numGap = scannerPictureNum - pictureScanner.getChildren().size();
            while(numGap > 0){
                if(head<0&&tail>=pictures.length)break;
                if((addFlag &&tail<pictures.length)||head<0) {
                    SlideThumbnail temp = new SlideThumbnail(pictures[tail]);
                    pictureScanner.getChildren().add(temp);
                    tail++;
                    numGap--;
                    addFlag = false;
                    continue;
                }
                if((!addFlag &&head>=0)||tail>=pictures.length) {
                    SlideThumbnail temp = new SlideThumbnail(pictures[head]);
                    pictureScanner.getChildren().add(0, temp);
                    head--;
                    numGap--;
                    addFlag = true;
                }
            }
            while(numGap < 0){
                if(delFlag) {
                    if(tail-2 <= currentIndex){
                        delFlag=false;
                        continue;
                    }
                    pictureScanner.getChildren().remove(pictureScanner.getChildren().size() - 1);
                    tail--;
                    numGap++;
                    delFlag=false;
                    continue;
                }
                if(!delFlag){
                    if(head+2 >= currentIndex){
                        delFlag=true;
                        continue;
                    }
                    pictureScanner.getChildren().remove(0);
                    head++;
                    numGap++;
                    delFlag=true;
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
                if(isMouseInToolBar)return;
                Platform.runLater(()->{
                    if(!mainPane.getChildren().contains(toolBar))return;
                    pictureShower.getParent().requestFocus(); //当ToolBar被移除时，焦点转移到BoardPane上，确保键盘监听正常
                    mainPane.getChildren().remove(toolBar);
                });
            }
        };
        timer.schedule(task,0,5000);
    }

    private void pictureLoad(){
        File[] pictures = SlideFileManager.getPictures();
        int currentIndex = SlideFileManager.getCurrentIndex();
        head = currentIndex-1;
        tail = currentIndex+1;
        //初步加载
        SlideThumbnail first = new SlideThumbnail(pictures[currentIndex]);
        first.setSelectedStyle();
        pictureScanner.getChildren().add(first);
        while(tail-head<8&&(head>=0||tail<pictures.length)){
            if(tail-head<8&&head>=0) {
                SlideThumbnail slideThumbnail = new SlideThumbnail(pictures[head]);
                pictureScanner.getChildren().add(0,slideThumbnail);
                head--;
            }
            if(tail-head<8&&tail<pictures.length){
                SlideThumbnail slideThumbnail = new SlideThumbnail(pictures[tail]);
                pictureScanner.getChildren().add(slideThumbnail);
                tail++;
            }
        }
    }

    private void indexListen(){
        SlideFileManager.currentIndexPropertyProperty().addListener(((observableValue, oldValue,newValue) -> {
            File[] pictures = SlideFileManager.getPictures();
            if(newValue.intValue() < oldValue.intValue()){
                if (pictureScanner.getChildren().size() > oldValue.intValue() - head - 1)
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
                if (pictureScanner.getChildren().size() > oldValue.intValue() - head - 1)
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

    private void lengthListen(){
        SlideFileManager.picturesLengthProperty().addListener(((observableValue, oldValue, newValue) -> {
            SlideListener.pictureScanner.getChildren().clear();
            this.pictureLoad();
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

    private void setIcon(){
        ImageView image = new ImageView(new Image(Objects.requireNonNull(SlideListener.class.getResourceAsStream("/top/ithaic/icons/play.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(slidePlay.getPrefWidth());
        image.setFitHeight(slidePlay.getPrefHeight());
        slidePlay.setGraphic(image);

        image = new ImageView(new Image(Objects.requireNonNull(SlideListener.class.getResourceAsStream("/top/ithaic/icons/back.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(lastPicture.getPrefWidth());
        image.setFitHeight(lastPicture.getPrefHeight());
        lastPicture.setGraphic(image);

        image = new ImageView(new Image(Objects.requireNonNull(SlideListener.class.getResourceAsStream("/top/ithaic/icons/forward.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(nextPicture.getPrefWidth());
        image.setFitHeight(nextPicture.getPrefHeight());
        nextPicture.setGraphic(image);

        image = new ImageView(new Image(Objects.requireNonNull(SlideListener.class.getResourceAsStream("/top/ithaic/icons/amplify.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(amplifyPicture.getPrefWidth());
        image.setFitHeight(amplifyPicture.getPrefHeight());
        amplifyPicture.setGraphic(image);

        image = new ImageView(new Image(Objects.requireNonNull(SlideListener.class.getResourceAsStream("/top/ithaic/icons/shrink.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(shrinkPicture.getPrefWidth());
        image.setFitHeight(shrinkPicture.getPrefHeight());
        shrinkPicture.setGraphic(image);

        image = new ImageView(new Image(Objects.requireNonNull(SlideListener.class.getResourceAsStream("/top/ithaic/icons/leftRotate.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(leftRotatePicture.getPrefWidth());
        image.setFitHeight(leftRotatePicture.getPrefHeight());
        leftRotatePicture.setGraphic(image);

        image = new ImageView(new Image(Objects.requireNonNull(SlideListener.class.getResourceAsStream("/top/ithaic/icons/rightRotate.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(rightRotatePicture.getPrefWidth());
        image.setFitHeight(rightRotatePicture.getPrefHeight());
        rightRotatePicture.setGraphic(image);

        image = new ImageView(new Image(Objects.requireNonNull(SlideListener.class.getResourceAsStream("/top/ithaic/icons/compress.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(compressImage.getPrefWidth());
        image.setFitHeight(compressImage.getPrefHeight());
        compressImage.setGraphic(image);
    }

    public static FlowPane getPictureScanner() {
        return pictureScanner;
    }
    public static int getHead() {
        return head;
    }
    public static void setHead(int head) {
        SlideListener.head = head;
    }
    public static int getTail() {
        return tail;
    }
    public static void setTail(int tail) {
        SlideListener.tail = tail;
    }
}
