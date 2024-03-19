package top.ithaic.shower.slidePlay;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.shower.PictureShower;

import java.io.File;
import java.util.ArrayList;


public final class SlidePlay {
    private static Stage playStage;
    private static Scene playScene;
    private static AnchorPane mainPane;
    private static StackPane subPane;
    private static ImageView imageView;
    private static Button exitButton;

    public SlidePlay() {
    }
    private static void initStyle(){
        playStage = new Stage();
        mainPane = new AnchorPane();
        subPane = new StackPane();
        imageView = new ImageView();
        exitButton = new Button("Exit");
        playScene = new Scene(mainPane);

        exitButton.setLayoutX(1375);
        exitButton.setLayoutY(800);
        exitButton.setPrefHeight(100);
        exitButton.setPrefWidth(250);
        exitButton.setFont(Font.font(30));

        subPane.getChildren().add(exitButton);
        mainPane.getChildren().add(subPane);

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

        playStage.setScene(playScene);
        playStage.show();
    }

    //TODO 为主窗口设计,供外部调用
//    public static void playPicture(){
//        initStyle();
//
//
//    }
    private static int i = 0;
    public static void playPicture(){
        initStyle();
        FlowPane flowPane = PictureShower.getThumbnails();
        mainPane.getChildren().add(imageView);
        ArrayList<File> arrayList = new ArrayList<>();
        for(Node node : flowPane.getChildren()){
            arrayList.add(((Thumbnail)node).getImageFile());
        }
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(2),event->{
            Image image = new Image(arrayList.get(i).toString());
            imageView.setImage(image);
            i++;
        }));
        delay.setCycleCount(arrayList.size());
        delay.play();

    }

    private static void sleep(int i) {
    }


}
