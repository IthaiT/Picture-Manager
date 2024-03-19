package top.ithaic.shower.SlideShower;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


public class SlideShower {
    private static ScaleTransitionThread scaleTransitionThread;
    private static double factor;
    private static Button shrinkPicture;
    private static Button amplifyPicture;
    private static Button lastPicture;
    private static Button nextPicture;
    private static Pane pictureShower;
    private static Canvas canvas;
    private static Image image;

    public SlideShower() {
    }

    public SlideShower(Pane pictureShower, Button lastPicture, Button nextPicture, Button amplifyPicture, Button shrinkPicture) {
        SlideShower.pictureShower = pictureShower;
        SlideShower.lastPicture = lastPicture;
        SlideShower.nextPicture = nextPicture;
        SlideShower.amplifyPicture = amplifyPicture;
        SlideShower.shrinkPicture = shrinkPicture;
        SlideShower.factor = 0;
        SlideShower.scaleTransitionThread = null;
        showPicture();
    }

    private void showPicture() {
        pictureShower.getChildren().clear();
        double slideWidth = pictureShower.getWidth();
        double slideHeight = pictureShower.getHeight();

        image = new Image(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex()].toURI().toString());

        //初始化控件
        canvas = new Canvas(slideWidth, slideHeight);
        canvas.widthProperty().bind(pictureShower.widthProperty());
        canvas.heightProperty().bind(pictureShower.heightProperty());

        //画布监听
        canvas.widthProperty().addListener(((observableValue, number, t1) -> drawPicture(canvas, image, 1 + factor, 0, 0)));
        canvas.heightProperty().addListener(((observableValue, number, t1) -> drawPicture(canvas, image, 1 + factor, 0, 0)));

        //按钮监听
        if (SlideFileManager.getPictures().length == 1) {
            lastPicture.setVisible(false);
            nextPicture.setVisible(false);
        } else {
            lastPicture.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> lastPicture(canvas));
            nextPicture.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> nextPicture(canvas));
            pictureShower.getParent().addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.LEFT) lastPicture(canvas);
                if (keyEvent.getCode() == KeyCode.RIGHT) nextPicture(canvas);
            });
        }
        amplifyPicture.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> amplifyPicture(factor));
        shrinkPicture.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> shrinkPicture(factor));


        pictureShower.getChildren().add(canvas);
    }

    private void lastPicture(Canvas canvas) {
        if (SlideFileManager.getCurrentIndex() > 0) {
            factor = 0;
            image = new Image(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex() - 1].toURI().toString());
            SlideFileManager.setCurrentIndex(SlideFileManager.getCurrentIndex() - 1);
            drawPicture(canvas, image, 1 + factor, 0, 0);
        }
    }

    private void nextPicture(Canvas canvas) {
        if (SlideFileManager.getCurrentIndex() < SlideFileManager.getPictures().length - 1) {
            factor = 0;
            image = new Image(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex() + 1].toURI().toString());
            SlideFileManager.setCurrentIndex(SlideFileManager.getCurrentIndex() + 1);
            drawPicture(canvas, image, 1, 0, 0);
        }
    }

    /**
     * /
     * @param canvas 画布
     * @param image  图片
     * @param factor 放缩因子
     * @param detX  放缩位置偏移量X (距中心)
     * @param detY  放缩位置偏移量Y (距中心)
     */
    private void drawPicture(Canvas canvas, Image image, double factor, double detX, double detY) {
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
        gc.drawImage(image, (imageWidth * factor - imageWidth) / 2 +detX, (imageHeight * factor - imageHeight) / 2+detY,
                imageWidth - (imageWidth * factor - imageWidth) + +detX, imageHeight - (imageHeight * factor - imageHeight) +detY,
                (canvasWidth - scaledWidth) / 2, (canvasHeight - scaledHeight) / 2,
                scaledWidth, scaledHeight);
    }

    public void drawPicture() {
        image = new Image(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex()].toURI().toString());
        this.drawPicture(canvas, image, 1, 0, 0);
    }

    private void amplifyPicture(double factor) {
        if(scaleTransitionThread!=null&&scaleTransitionThread.isAlive())scaleTransitionThread.terminal();
        scaleTransitionThread = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor+=0.2);
        scaleTransitionThread.start();
    }

    private void shrinkPicture(double factor) {
        if(scaleTransitionThread!=null&&scaleTransitionThread.isAlive())scaleTransitionThread.terminal();
        scaleTransitionThread = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor-=0.2);
        scaleTransitionThread.start();
    }

    private class ScaleTransitionThread extends Thread{
        private double startFactor;
        private final double endFactor;
        private boolean isTerminal;

        public ScaleTransitionThread(double startFactor,double endFactor){
            this.startFactor = startFactor;
            this.endFactor = endFactor;
        }
        @Override
        public void run(){
            if(endFactor < startFactor){
                while(Math.abs(startFactor-endFactor) > 1e-5&&!isTerminal){
                    startFactor-=0.01;
                    drawPicture(canvas,image,1+startFactor,0,0);
                    try {
                        sleep(30);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            else{
                while(Math.abs(endFactor-startFactor) > 1e-5&&!isTerminal){
                    startFactor+=0.01;
                    drawPicture(canvas,image,1+startFactor,0,0);
                    try {
                        sleep(30);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        public void terminal(){
            this.isTerminal = true;
        }
    }

}

