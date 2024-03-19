package top.ithaic.shower.slidePlay;

import javafx.animation.PauseTransition;
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


public final class SlidePlay {
    private static Stage playStage;
    private static Scene playScene;
    private static Button exitButton;

    public SlidePlay() {
    }
    private static void initStyle(){
        playStage = new Stage();
        playStage.initStyle(StageStyle.TRANSPARENT); // Hide window decorations
        playStage.setFullScreen(true); // Fullscreen mode
        exitButton = new Button("Exit");
        exitButton.setLayoutX(1375);
        exitButton.setLayoutY(800);
        exitButton.setPrefHeight(100);
        exitButton.setPrefWidth(250);
        exitButton.setFont(Font.font(30));
    }

    //TODO 为主窗口设计,供外部调用
    public static void playPicture(){
        initStyle();
        FlowPane flowPane = PictureShower.getThumbnails();
        for(Node node :flowPane.getChildren()){
            Thumbnail thumbnail = (Thumbnail) node;
            playPicture(thumbnail);
        }

    }
    private static void playPicture(Thumbnail thumbnail){

        Image image = new Image(thumbnail.getImageFile().toString());
        ImageView imageView = new ImageView(image);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(imageView);


        StackPane buttonGroup = new StackPane();
        buttonGroup.getChildren().add(exitButton);
        anchorPane.getChildren().add(buttonGroup);


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
        playScene = new Scene(anchorPane);
        playStage.setScene(playScene);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.play();
        playStage.show();
    }



}
