package top.ithaic.shower.SlideShower;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import top.ithaic.Myinterface.Listener;

public class SlideShower{
    private Pane pictureShower;

    public SlideShower(Pane pictureShower){
        this.pictureShower = pictureShower;
        Listen();
    }

    public void Listen(){
        BorderPane borderPane = (BorderPane) this.pictureShower.getParent();
        borderPane.widthProperty().addListener((observableValue, number, t1) -> {
            show();
        });
    }

    private void show(){
        this.pictureShower.getChildren().clear();

        double slideWidth = this.pictureShower.getWidth();
        double slideHeight = this.pictureShower.getHeight();
        System.out.println(slideWidth);
        System.out.println(slideHeight);
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

