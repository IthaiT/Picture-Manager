package top.ithaic.imageview;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static javafx.scene.paint.Color.rgb;

public class Thumbnail extends BorderPane {
    private static double thumbnailWidth = 140;
    private static double thumbnailHeight = 100;
    private boolean isClicked = false;
    private File imageFile;
    private StackPane stackPane;
    private Label label;
    private boolean isLabelClicked = false;

    public Thumbnail() {}

    public Thumbnail(File imageFile) {
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

        //设置图片label
        label = new Label(imageFile.getName());
        label.prefWidthProperty().bind(stackPane.widthProperty());
        label.setAlignment(Pos.CENTER);

        this.setCenter(stackPane);
        this.setBottom(label);
        this.setMaxWidth(thumbnailWidth + 10);

        stackPane.getStyleClass().add("thumbnail-default");
        label.getStyleClass().add("textField-default");
    }


    public double getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(double thumbnailWidth) {
        Thumbnail.thumbnailWidth = thumbnailWidth;
    }

    public double getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(double thumbnailHeight) {
        Thumbnail.thumbnailHeight = thumbnailHeight;
    }

    public File getImageFile() {
        return this.imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public Label getLabel() {
        return label;
    }

    public boolean getIsClicked() {
        return this.isClicked;
    }

    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public boolean getIsLabelClicked(){
        return isLabelClicked;
    }
    public void  setIsLabelClicked(boolean isLabelClicked){
         this.isLabelClicked = isLabelClicked;
    }
    public void setSelectedStyle() {
        stackPane.getStyleClass().remove("thumbnail-default");
        stackPane.getStyleClass().add("thumbnail-hover");
        label.setTextFill(Color.BLACK);
        label.setBackground(new Background(new BackgroundFill(rgb(28, 136, 203), null, null)));
        System.out.println("ok1");
        labelListener(); //被点击之后监听label有没有被点击
    }

    public void setUnSelectedStyle() {
        stackPane.getStyleClass().remove("thumbnail-hover");
        stackPane.getStyleClass().add("thumbnail-default");
        label.setBackground(new Background(new BackgroundFill(rgb(247, 247, 247), null, null)));
        label.setOnMouseClicked(null);
        stackPane.setOnMouseClicked(null);
        isLabelClicked = false;
    }
    public void labelListener(){
        label.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount() >= 1){
                isLabelClicked = true;
            }
        });
        stackPane.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount() >= 1){
                isLabelClicked = false;
            }
        });
    }

}