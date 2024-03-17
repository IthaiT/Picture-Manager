package top.ithaic.shower.SlideShower;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SlideShower {
    private final Pane pictureShower;

    public SlideShower(Pane pictureShower){
        this.pictureShower = pictureShower;
        show();
    }

    private void show(){

        double slideWidth = this.pictureShower.getPrefWidth();
        double slideHeight = this.pictureShower.getPrefHeight();

        //初始化控件
        Canvas canvas = new Canvas(slideWidth, slideHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //加载图片
        Image image = new Image(SlideFileManager.getPicture().toURI().toString(), slideWidth,slideHeight, true, true);
        double x = (slideWidth - image.getWidth()) / 2;
        double y = (slideHeight - image.getHeight()) / 2;
        gc.drawImage(image, x, y);
        this.pictureShower.getChildren().add(canvas);
    }
}
