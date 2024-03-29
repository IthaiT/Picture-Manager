package top.ithaic.shower.SlideShower;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


public class SlideShower {
    private final double FACTORINCREMENT = 0.15;
    private static ScaleTransitionThread scaleTransitionThread;
    private static double factor;
    private static Button shrinkPicture;
    private static Button amplifyPicture;
    private static Button lastPicture;
    private static Button nextPicture;
    private static Pane pictureShower;
    private static Canvas canvas;
    private static Image image;
    private long lastScrollTime;
    private long lastMouseTime;
    private double offsetX = 0;
    private double offsetY = 0;
    private double lastDetX = 0;
    private double lastDetY = 0;
    private double dx = 0;
    private double dy = 0;
    public SlideShower() {
    }

    public SlideShower(Pane pictureShower, Button lastPicture, Button nextPicture, Button amplifyPicture, Button shrinkPicture, Button slidePlay) {
        SlideShower.pictureShower = pictureShower;
        SlideShower.lastPicture = lastPicture;
        SlideShower.nextPicture = nextPicture;
        SlideShower.amplifyPicture = amplifyPicture;
        SlideShower.shrinkPicture = shrinkPicture;
        SlideShower.factor = 0;
        SlideShower.scaleTransitionThread = null;
        initPicture();
        initMouseListen();
        initCanvasEvent();
    }

    private void initPicture() {
        pictureShower.getChildren().remove(canvas);
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

        pictureShower.getChildren().add(canvas);
    }

    private void lastPicture(Canvas canvas) {
        if (SlideFileManager.getCurrentIndex() > 0) {
            factor = 0;
            lastDetX = 0;
            lastDetY = 0;
            image = new Image(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex() - 1].toURI().toString());
            SlideFileManager.setCurrentIndex(SlideFileManager.getCurrentIndex() - 1);
            drawPicture(canvas, image, 1 + factor, 0, 0);
        }
    }

    private void nextPicture(Canvas canvas) {
        if (SlideFileManager.getCurrentIndex() < SlideFileManager.getPictures().length - 1) {
            factor = 0;
            lastDetX = 0;
            lastDetY = 0;
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
     * @param detX  放缩位置偏移量X
     * @param detY  放缩位置偏移量Y
     */
    private void drawPicture(Canvas canvas, Image image, double factor, double detX, double detY) {
            double canvasWidth = canvas.getWidth();
            double canvasHeight = canvas.getHeight();

            // 获取图片的原始尺寸
            double imageWidth = image.getWidth();
            double imageHeight = image.getHeight();

            //  适应画布
            double scaleFactorWidth = canvasWidth / imageWidth;
            double scaleFactorHeight = canvasHeight / imageHeight;
            double tmpFactor = Math.min(scaleFactorWidth, scaleFactorHeight); // 选择较小的因子以保持纵横比
            factor*=tmpFactor;

            // 根据factor计算缩放后的尺寸
            double scaledWidth = imageWidth * factor;
            double scaledHeight = imageHeight * factor;

            // 计算图像在Canvas上的位置，使其居中
            double offsetX = (canvasWidth - scaledWidth) / 2;
            double offsetY = (canvasHeight - scaledHeight) / 2;

            // 确保图像不会超出Canvas边界
            offsetX =  Math.min(offsetX, canvasWidth - scaledWidth);
            offsetY =  Math.min(offsetY, canvasHeight - scaledHeight);

            // 获取GraphicsContext对象并清除Canvas
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvasWidth, canvasHeight);

            // 在Canvas上绘制缩放后的图像
            gc.drawImage(image, offsetX + detX + lastDetX, offsetY + detY + lastDetY, scaledWidth, scaledHeight);
    }

    public void drawPicture() {
        image = new Image(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex()].toURI().toString());
        this.drawPicture(canvas, image, 1, 0, 0);
    }

    private void amplifyPicture() {
        if(scaleTransitionThread!=null&&scaleTransitionThread.isAlive())scaleTransitionThread.terminal();
        scaleTransitionThread = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor+=FACTORINCREMENT,0,0);
        scaleTransitionThread.start();
    }

    private void shrinkPicture() {
        if(scaleTransitionThread!=null&&scaleTransitionThread.isAlive())scaleTransitionThread.terminal();
        scaleTransitionThread = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor-=FACTORINCREMENT,0,0);
        scaleTransitionThread.start();
    }

    private void initMouseListen(){
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
        amplifyPicture.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> amplifyPicture());
        shrinkPicture.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> shrinkPicture());


        SlideShower.canvas.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastMouseTime < 300) {
                return;
            }
            if(scaleTransitionThread!=null&&scaleTransitionThread.isAlive())scaleTransitionThread.terminal();
            if(mouseEvent.isControlDown()){
                ScaleTransitionThread scaleTransitionThread1 = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor+=FACTORINCREMENT,0,0);
                scaleTransitionThread1.start();
            }
            if(mouseEvent.isAltDown()){
                ScaleTransitionThread scaleTransitionThread1 = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor-=FACTORINCREMENT,0,0);
                scaleTransitionThread1.start();
            }
            lastMouseTime = currentTime;
        });
        SlideShower.canvas.setOnScroll(scrollEvent -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastScrollTime < 300) {
                return;
            }
            if(scaleTransitionThread!=null&&scaleTransitionThread.isAlive())scaleTransitionThread.terminal();
            if(scrollEvent.getDeltaY()>0){
                ScaleTransitionThread scaleTransitionThread1 = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor+=FACTORINCREMENT,0,0);
                scaleTransitionThread1.start();
            }
            if(scrollEvent.getDeltaY()<0){
                ScaleTransitionThread scaleTransitionThread1 = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor-=FACTORINCREMENT,0,0);
                scaleTransitionThread1.start();
            }
            lastScrollTime = currentTime;
        });
    }

    private void initCanvasEvent(){
        // 添加鼠标事件监听器
        canvas.setOnMousePressed(event -> {
//            if(factor<=0)return;
            offsetX = event.getX();
            offsetY = event.getY();
        });

        canvas.setOnMouseDragged(event -> {
//            if(factor<=0)return;
            // 更新图片位置
            dx = event.getX() - offsetX;
            dy = event.getY() - offsetY;
            // 重绘图片
            drawPicture(canvas, image, 1+factor, dx, dy);
        });

        canvas.setOnMouseReleased(event->{
            lastDetX = dx + lastDetX;
            lastDetY = dy + lastDetY;
        });

    }


    private class ScaleTransitionThread extends Thread{
        private double startFactor;
        private final double endFactor;
        private boolean isTerminal;
        private double detX;
        private double detY;

        public ScaleTransitionThread(double startFactor,double endFactor,double detX,double detY){
            this.startFactor = startFactor;
            this.endFactor = endFactor;
            this.detX = detX;
            this.detY = detY;
        }
        @Override
        public void run(){
            //倍数为负时，不能缩小，否则异常
            if(1+endFactor<0){
                SlideShower.factor+=FACTORINCREMENT;
                return;
            }
            if(endFactor < startFactor){
                while(Math.abs(startFactor-endFactor) > 1e-5&&!isTerminal){
                    startFactor-=0.01;
                    drawPicture(canvas,image,1+startFactor,detX,detY);
                    try {
                        sleep(15);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            else{
                while(Math.abs(endFactor-startFactor) > 1e-5&&!isTerminal){
                    startFactor+=0.01;
                    drawPicture(canvas,image,1+startFactor,detX,detY);
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

