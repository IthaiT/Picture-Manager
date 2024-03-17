package top.ithaic.shower.SlideShower;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.io.File;

public class SlideShower{
    private Button lastPicture;
    private Button nextPicture;
    private Pane pictureShower;

    public SlideShower(Pane pictureShower,Button lastPicture,Button nextPicture){
        this.pictureShower = pictureShower;
        this.lastPicture = lastPicture;
        this.nextPicture = nextPicture;

        showPicture(SlideFileManager.getCurrentPicture());
    }

    private void showPicture(File picture){
        this.pictureShower.getChildren().clear();
        double slideWidth = this.pictureShower.getWidth();
        double slideHeight = this.pictureShower.getHeight();
        Image image = new Image(picture.toURI().toString());
        //初始化控件
        Canvas canvas = new Canvas(slideWidth, slideHeight);
        canvas.widthProperty().bind(this.pictureShower.widthProperty());
        canvas.heightProperty().bind(this.pictureShower.heightProperty());

        canvas.widthProperty().addListener(((observableValue, number, t1) -> drawPicture(canvas,image)));
        canvas.heightProperty().addListener(((observableValue, number, t1) -> drawPicture(canvas,image)));
        this.pictureShower.getChildren().add(canvas);
    }

    private void drawPicture(Canvas canvas,Image image){
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        // 保持图片宽高比
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        double scale = Math.min(canvasWidth / imageWidth, canvasHeight / imageHeight);
        double scaledWidth = imageWidth * scale;
        double scaledHeight = imageHeight * scale;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
        gc.drawImage(image, 0, 0, imageWidth, imageHeight,
                (canvasWidth - scaledWidth) / 2, (canvasHeight - scaledHeight) / 2,
                scaledWidth, scaledHeight);
    }
}

