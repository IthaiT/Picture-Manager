package top.ithaic.imageview;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.File;

import static javafx.scene.paint.Color.rgb;

public class SlideThumbnail extends Thumbnail {
    private static double thumbnailWidth = 80;
    private static double thumbnailHeight = 60;
    private StackPane stackPane;
    private File imageFile;
    public SlideThumbnail(){}
    public SlideThumbnail(File imageFile){
        this.imageFile = imageFile;
        Canvas canvas = new Canvas(thumbnailWidth, thumbnailHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //加载并画出图片
        Image image = new Image(imageFile.toURI().toString(), thumbnailWidth, thumbnailHeight, true, true);
        double x = (thumbnailWidth - image.getWidth()) / 2;
        double y = (thumbnailHeight - image.getHeight()) / 2;
        gc.drawImage(image, x, y);

        //将画布放在stackPane容器中
        stackPane = new StackPane();
        stackPane.getChildren().add(canvas);

        this.setCenter(stackPane);
        this.setMaxWidth(thumbnailWidth+10);

        stackPane.getStyleClass().add("slideThumbnail-default");
    }

    @Override
    public double getThumbnailWidth() {
        return thumbnailWidth;
    }

    @Override
    public double getThumbnailHeight() {
        return thumbnailHeight;
    }

    @Override
    public void setSelectedStyle() {
        stackPane.getStyleClass().remove("slideThumbnail-default");
        stackPane.getStyleClass().add("slideThumbnail-hover");
    }
    @Override
    public void setUnSelectedStyle() {
        stackPane.getStyleClass().remove("slideThumbnail-hover");
        stackPane.getStyleClass().add("slideThumbnail-default");
    }
    @Override
    public File getImageFile() {
        return imageFile;
    }
}
