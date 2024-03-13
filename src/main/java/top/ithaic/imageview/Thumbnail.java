package top.ithaic.imageview;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import java.io.File;

public class Thumbnail extends BorderPane {
    private static double thumbnailWidth = 140;
    private static double thumbnailHeight = 100;
    public Thumbnail(){}
    public Thumbnail(File imagefile){
        Canvas canvas = new Canvas(thumbnailWidth, thumbnailHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image image = new Image(imagefile.toString(), thumbnailWidth -2, thumbnailHeight -2, true, true);
        double x = (thumbnailWidth - image.getWidth()) / 2;
        double y = (thumbnailHeight - image.getHeight()) /2;
        gc.drawImage(image, x, y);

        Label label = new Label(imagefile.getName());
        label.prefWidthProperty().bind(canvas.widthProperty());
        label.setAlignment(Pos.CENTER);

        this.setCenter(canvas);
        this.setBottom(label);

        this.setMaxWidth(thumbnailWidth + 10);
        this.getStyleClass().add("thumbnail-default");
        this.setOnMouseEntered(
                event -> this.getStyleClass().add("thumbnail-hover"));
        this.setOnMouseExited(
                event -> this.getStyleClass().remove("thumbnail-hover"));
    }

    public double getThumbnailWidth() {
        return thumbnailWidth;
    }
    public void setThumbnailWidth(double thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }
    public double getThumbnailHeight() {
        return thumbnailHeight;
    }
    public void setThumbnailHeight(double thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

}
