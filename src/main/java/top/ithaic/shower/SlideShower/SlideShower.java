package top.ithaic.shower.SlideShower;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


public class SlideShower{
    private final Button lastPicture;
    private final Button nextPicture;
    private Pane pictureShower;
    private static Image image;

    public SlideShower(Pane pictureShower,Button lastPicture,Button nextPicture){
        this.pictureShower = pictureShower;
        this.lastPicture = lastPicture;
        this.nextPicture = nextPicture;
        SlideFileManager.setCurrentIndex(0);
        showPicture();
    }

    private void showPicture(){
        this.pictureShower.getChildren().clear();
        double slideWidth = this.pictureShower.getWidth();
        double slideHeight = this.pictureShower.getHeight();

        image = new Image(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex()].toURI().toString());

        //初始化控件
        Canvas canvas = new Canvas(slideWidth, slideHeight);
        canvas.widthProperty().bind(this.pictureShower.widthProperty());
        canvas.heightProperty().bind(this.pictureShower.heightProperty());

        //画布监听
        canvas.widthProperty().addListener(((observableValue, number, t1) -> drawPicture(canvas, image)));
        canvas.heightProperty().addListener(((observableValue, number, t1) -> drawPicture(canvas, image)));

        //按钮监听
        if(SlideFileManager.getPictures().length==1){
            lastPicture.setVisible(false);
            nextPicture.setVisible(false);
        }
        else{
            lastPicture.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
                if(SlideFileManager.getCurrentIndex()>0) {
                    System.out.println(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex()-1].getName());
                    image = new Image(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex()-1].toURI().toString());
                    SlideFileManager.setCurrentIndex(SlideFileManager.getCurrentIndex()-1);
                    drawPicture(canvas,image);
                }
            });
            nextPicture.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
                if(SlideFileManager.getCurrentIndex()<SlideFileManager.getPictures().length-1) {
                    System.out.println(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex()+1].getName());
                    image = new Image(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex()+1].toURI().toString());
                    SlideFileManager.setCurrentIndex(SlideFileManager.getCurrentIndex()+1);
                    drawPicture(canvas,image);
                }
            });
        }


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

