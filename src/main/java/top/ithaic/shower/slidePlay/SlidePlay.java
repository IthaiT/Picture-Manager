package top.ithaic.shower.slidePlay;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.shower.PictureShower;

import java.io.File;
import java.util.*;


public final class SlidePlay {
    private static Stage playStage;
    private static Scene playScene;
    private static Pane mainPane;
    private static StackPane subPane;
    private static ImageView imageView;
    private static ToolBar toolBar;
    private static Button statusButton;
    private static Button reduceOneS;
    private static Label label;
    private static Button addOneS;
    private static Button exitButton;
    private static Timeline delay;//切换幻灯片的时间延迟
    private static Timer timer;//菜单栏隐藏的时间延迟
    public SlidePlay() {
    }
    private static void initStyle(){
        playStage = new Stage();
        subPane= new StackPane();
        mainPane = new Pane();
        imageView = new ImageView();
        toolBar = new ToolBar();
        statusButton = new Button("停止播放");
        reduceOneS = new Button("-1s");
        label = new Label("间隔2秒");
        addOneS = new Button("+1s");
        exitButton = new Button("退出");
        toolBar.getItems().addAll(statusButton,reduceOneS,label,addOneS,exitButton);
        playScene = new Scene(mainPane);

        imageView.setPreserveRatio(true);
        
        mainPane.getChildren().add(subPane);
        mainPane.setStyle("-fx-background-color: black;");
        subPane.getChildren().add(imageView);
        
        playStage.setFullScreen(true);
        playStage.setScene(playScene);

        playStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        Listen();
        startTimer();
    }

    private static int i = 0;
    private static int playTime = 2;
    //TODO for主窗口
    public static void playPicture(){
        initStyle();
        FlowPane flowPane = PictureShower.getThumbnails();
        ArrayList<File> arrayList = new ArrayList<>();
        int start = 0;//标记从哪张图片开始放映
        for(Node node : flowPane.getChildren()){
            Thumbnail thumbnail = (Thumbnail)node;
            arrayList.add(thumbnail.getImageFile());
            if(thumbnail.getIsClicked()){
                i = start;
            }
            start++;
        }
        if(arrayList.isEmpty())return;

        //显示窗口并使图片放置与中央
        playStage.show();
        imageView.setFitHeight(playStage.getHeight());
        imageView.setFitWidth(playStage.getWidth());
        subPane.setPrefWidth(playStage.getWidth());
        subPane.setPrefHeight(playStage.getHeight());

        imageView.setImage(new Image(arrayList.get(i).toString()));i++;
        delay = new Timeline(new KeyFrame(Duration.seconds(playTime),event->{
            if(i == arrayList.size()){
                i = 0;//从头开始放映
            }
            Image image = new Image(arrayList.get(i).toString());
            imageView.setImage(image);
            i++;
        }));
        delay.setCycleCount(Animation.INDEFINITE);
        delay.play();
    }

    //TODO for 幻灯片窗口
    public static void playPicture(File[] pictures,int index){
        initStyle();
        i = index;
        if(pictures.length == 0)return;
        //显示窗口并使图片放置与中央
        playStage.show();
        imageView.setFitHeight(playStage.getHeight());
        imageView.setFitWidth(playStage.getWidth());
        subPane.setPrefWidth(playStage.getWidth());
        subPane.setPrefHeight(playStage.getHeight());

        imageView.setImage(new Image(pictures[i].toString()));i++;
        delay = new Timeline(new KeyFrame(Duration.seconds(playTime),event->{
            if(i == pictures.length){
                i = 0;//从头开始放映
            }
            Image image = new Image(pictures[i].toString());
            imageView.setImage(image);
            i++;
        }));
        delay.setCycleCount(Animation.INDEFINITE);
        delay.play();
    }

    public static void startTimer(){
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    System.out.println("测试是否还在执行");
                    if(!mainPane.getChildren().contains(toolBar))return;
                    mainPane.getChildren().remove(toolBar);

                });
            }
        };
        timer.schedule(task,0,5000);
    }

    public static void Listen(){
        //TODO 窗口关闭，将时间计数器关闭
        playStage.setOnCloseRequest(windowEvent -> {
            if(delay != null){
                delay.stop();
                delay = null;
            }
            timer.cancel();
            i = 0;
        });

        //TODO
        playScene.setOnMouseMoved(mouseEvent -> {
            if(mainPane.getChildren().contains(toolBar))return;
            mainPane.getChildren().add(toolBar);
            //TODO 这里有问题，第一次加入的时候，meunubar的宽度为0
            toolBar.setLayoutX((mainPane.getWidth()-toolBar.getWidth())/2);
            toolBar.setLayoutY(mainPane.getHeight()-100);

        });

        statusButton.setOnMouseClicked(mouseEvent -> {
            String text = statusButton.getText();
            if(text.equals("停止播放")){
                statusButton.setText("继续播放");
                delay.stop();
            }
            else if(text.equals("继续播放")){
                statusButton.setText("停止播放");
                delay.play();
            }
        });

        reduceOneS.setOnMouseClicked(mouseEvent -> {
            if(playTime <= 1) return;
            playTime--;
            delay.stop();
            delay.setDelay(Duration.seconds(playTime));
            delay.play();
            label.setText("间隔"+playTime +"秒");
        });
        addOneS.setOnMouseClicked(mouseEvent -> {
            if(playTime >= 20)return;
            playTime++;
            delay.stop();
            delay.setDelay(Duration.seconds(playTime));
            delay.play();
            label.setText("间隔"+playTime +"秒");
        });
        exitButton.setOnMouseClicked((mouseEvent -> {
            playStage.close();
        }));
    }
}




