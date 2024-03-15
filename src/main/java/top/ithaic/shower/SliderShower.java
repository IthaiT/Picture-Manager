package top.ithaic.shower;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;

public class SliderShower {
    private static double slideWidth = 700;
    private static double slideHeight = 500;
    private static File picture;

    public SliderShower(File picture){
        SliderShower.picture = picture;
        initStage();
    }

    private void initStage(){
        //初始化控件
        Canvas canvas = new Canvas(slideWidth, slideHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //加载图片
        Image image = new Image(picture.toURI().toString(), slideWidth,slideHeight, true, true);
        double x = (slideWidth - image.getWidth()) / 2;
        double y = (slideHeight - image.getHeight()) / 2;
        gc.drawImage(image, x, y);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(canvas);

        Scene scene = new Scene(stackPane);

        //初始化舞台
        Stage sliderStage = new Stage();
        sliderStage.setTitle(picture.getName());
        sliderStage.setMinWidth(800);
        sliderStage.setMinHeight(600);
        sliderStage.setScene(scene);
        sliderStage.show();
    }
}
