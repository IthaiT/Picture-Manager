package top.ithaic.shower.slidePlay;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.shower.PictureShower;
import top.ithaic.shower.SlideShower.SlideFileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public final class SlidePlay {
    private static Stage playStage;
    private static Scene playScene;
    private static AnchorPane subPane;
    private static Pane mainPane;
    private static ImageView imageView;
    private static Button exitButton;
    private static Timeline delay;
    public SlidePlay() {
    }
    private static void initStyle(){
        playStage = new Stage();
        subPane= new AnchorPane();
        mainPane = new Pane();
        imageView = new ImageView();
        exitButton = new Button("Exit");
        playScene = new Scene(mainPane);

        exitButton.setLayoutX(1375);
        exitButton.setLayoutY(800);
        exitButton.setPrefHeight(100);
        exitButton.setPrefWidth(250);
        exitButton.setFont(Font.font(30));

        mainPane.getChildren().add(subPane);
        mainPane.getChildren().add(exitButton);
        subPane.getChildren().add(imageView);


        playStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            double scaleFactor = newValue.doubleValue() / 1080;
            imageView.setFitHeight(1080 * scaleFactor);
            exitButton.setPrefHeight(100 * scaleFactor);
            exitButton.setFont(Font.font(30 * scaleFactor));
            exitButton.setLayoutY(800 * scaleFactor);
        });

        playStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            double scaleFactor = newValue.doubleValue() / 1920;
            imageView.setFitWidth(1920 * scaleFactor);
            exitButton.setPrefWidth(250 * scaleFactor);
            exitButton.setFont(Font.font(30 * scaleFactor));
            exitButton.setLayoutX(1375 * scaleFactor);
        });

        playStage.setFullScreen(true);
        playStage.setScene(playScene);
        Listen();
    }

    private static int i = 0;
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
        playStage.show();
        imageView.setImage(new Image(arrayList.get(i).toString()));i++;
        delay = new Timeline(new KeyFrame(Duration.seconds(2),event->{
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
        playStage.show();
        imageView.setImage(new Image(pictures[i].toString()));i++;
        delay = new Timeline(new KeyFrame(Duration.seconds(2),event->{
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

    public static void Listen(){
        exitButton.setOnMouseClicked(mouseEvent -> {
            if(delay != null){
                delay.stop();
                delay = null;
            }
            i =0;
            playStage.close();
        });
    }
}




