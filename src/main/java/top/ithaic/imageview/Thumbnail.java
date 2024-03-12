package top.ithaic.imageview;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import java.io.File;

public class Thumbnail extends BorderPane {
    public static final double THUMBNAIL_WIDTH = 160;
    public static final double THUMBNAIL_HEIGHT = 120;

    public Thumbnail(File imagefile){
        Canvas canvas = new Canvas(THUMBNAIL_WIDTH,THUMBNAIL_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image image = new Image(imagefile.toString(), THUMBNAIL_WIDTH -2, THUMBNAIL_HEIGHT -2, true, true);
        double x = (THUMBNAIL_WIDTH - image.getWidth()) / 2;
        double y = (THUMBNAIL_HEIGHT - image.getHeight()) /2;
        gc.drawImage(image, x, y);

        Label label = new Label(imagefile.getName());
        label.prefWidthProperty().bind(canvas.widthProperty());
        label.setAlignment(Pos.CENTER);

        this.setCenter(canvas);
        this.setBottom(label);

        this.setMaxWidth(THUMBNAIL_WIDTH + 10);
        this.getStyleClass().add("thumbnail-default");
        this.setOnMouseEntered(
                event -> this.getStyleClass().add("thumbnail-hover"));
        this.setOnMouseExited(
                event -> this.getStyleClass().remove("thumbnail-hover"));
    }
}
