package top.ithaic.shower.SlideShower;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


public class SlideShower {
    private final double FACTORINCREMENT = 0.15;
    private static ScaleTransitionThread scaleTransitionThread;
    private static double factor;
    private static Pane pictureShower;
    private static Canvas canvas;
    private static Image image;
    private static double historyAngle;
    private long lastScrollTime;
    private long lastMouseTime;
    private static double offsetX = 0;
    private static double offsetY = 0;
    private static double lastDetX = 0;
    private static double lastDetY = 0;
    private static double dx = 0;
    private static double dy = 0;
    public SlideShower() {
    }
    public SlideShower(Pane pictureShower) {
        SlideShower.pictureShower = pictureShower;
        SlideShower.factor = 0;
        SlideShower.scaleTransitionThread = null;
        SlideShower.historyAngle = 0.0;
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
        canvas.widthProperty().addListener(((observableValue, number, t1) -> drawPicture(canvas, image, 1 + factor, 0, 0,0)));
        canvas.heightProperty().addListener(((observableValue, number, t1) -> drawPicture(canvas, image, 1 + factor, 0, 0,0)));

        pictureShower.getChildren().add(canvas);
    }
    public void lastPicture() {
        if (SlideFileManager.getCurrentIndex() > 0) {
            recoverPicture();
            image = new Image(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex() - 1].toURI().toString());
            SlideFileManager.setCurrentIndex(SlideFileManager.getCurrentIndex() - 1);
            SlideFileManager.setCurrentIndexProperty(SlideFileManager.getCurrentIndex());
            drawPicture(canvas, image, 1 + factor, 0, 0,0);
        }
    }
    public void nextPicture() {
        if (SlideFileManager.getCurrentIndex() < SlideFileManager.getPictures().length - 1) {
            recoverPicture();
            image = new Image(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex() + 1].toURI().toString());
            SlideFileManager.setCurrentIndex(SlideFileManager.getCurrentIndex() + 1);
            SlideFileManager.setCurrentIndexProperty(SlideFileManager.getCurrentIndex());
            drawPicture(canvas, image, 1+factor, 0, 0,0);
        }
    }
    public void amplifyPicture() {
        if(scaleTransitionThread!=null&&scaleTransitionThread.isAlive())scaleTransitionThread.terminal();
        scaleTransitionThread = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor+=FACTORINCREMENT);
        scaleTransitionThread.start();
    }

    public void shrinkPicture() {
        if(scaleTransitionThread!=null&&scaleTransitionThread.isAlive())scaleTransitionThread.terminal();
        scaleTransitionThread = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor-=FACTORINCREMENT);
        scaleTransitionThread.start();
    }

    public void rotatePicture(double angle){
        drawPicture(canvas,image,1+factor,dx,dy,angle);
    }
    /**
     * /
     * @param canvas 画布
     * @param image  图片
     * @param factor 放缩因子
     * @param detX  放缩位置偏移量X
     * @param detY  放缩位置偏移量Y
     */
    private void drawPicture(Canvas canvas, Image image, double factor, double detX, double detY,double angle) {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        // 获取图片的原始尺寸
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        //  适应画布
        double scaleFactorWidth = canvasWidth / imageWidth;
        double scaleFactorHeight = canvasHeight / imageHeight;
        double tmpFactor = Math.min(scaleFactorWidth, scaleFactorHeight); // 选择较小的因子以保持纵横比
        factor *= tmpFactor;

        // 根据factor计算缩放后的尺寸
        double scaledWidth = imageWidth * factor;
        double scaledHeight = imageHeight * factor;

        // 计算图像在Canvas上的位置，使其居中
        double offsetX = (canvasWidth - scaledWidth) / 2;
        double offsetY = (canvasHeight - scaledHeight) / 2;

        // 确保图像不会超出Canvas边界
        offsetX = Math.min(offsetX, canvasWidth - scaledWidth);
        offsetY = Math.min(offsetY, canvasHeight - scaledHeight);

        // 获取GraphicsContext对象并清除Canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
        gc.save();
        gc.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        gc.rotate(historyAngle + angle);
        historyAngle = historyAngle + angle;
        gc.translate(-canvas.getWidth() / 2, -canvas.getHeight() / 2);
        //存在旋转角度时处理鼠标拖动
        double temp;
        double X = detX + lastDetX;
        double Y = detY + lastDetY;
        if (historyAngle % 360 == 90 || historyAngle % 360 == -270) {
            temp = X;
            X = Y;
            Y = -temp;
        } else if (historyAngle % 360 == 180 || historyAngle % 360 == -180) {
            X = -X;
            Y = -Y;
        } else if (historyAngle % 360 == 270 || historyAngle % 360 == -90) {
            temp = X;
            X = -Y;
            Y = temp;
        }
        // 在Canvas上绘制缩放后的图像
        gc.drawImage(image, offsetX + X, offsetY + Y, scaledWidth, scaledHeight);
        gc.restore();
    }

    public void drawPicture() {
        image = new Image(SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex()].toURI().toString());
        this.drawPicture(canvas, image, 1, 0, 0,0);
    }

    public static void recoverPicture(){
        factor = 0;
        offsetX = 0;
        offsetY = 0;
        lastDetX = 0;
        lastDetY = 0;
        dx = 0;
        dy = 0;
        historyAngle = 0.0;
    }

    private void initMouseListen(){
        SlideShower.canvas.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastMouseTime < 300) {
                return;
            }
            if(scaleTransitionThread!=null&&scaleTransitionThread.isAlive())scaleTransitionThread.terminal();
            if(mouseEvent.isControlDown()){
                ScaleTransitionThread scaleTransitionThread1 = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor+=FACTORINCREMENT);
                scaleTransitionThread1.start();
            }
            if(mouseEvent.isAltDown()){
                ScaleTransitionThread scaleTransitionThread1 = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor-=FACTORINCREMENT);
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
                ScaleTransitionThread scaleTransitionThread1 = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor+=FACTORINCREMENT);
                scaleTransitionThread1.start();
            }
            if(scrollEvent.getDeltaY()<0){
                ScaleTransitionThread scaleTransitionThread1 = new ScaleTransitionThread(SlideShower.factor,SlideShower.factor-=FACTORINCREMENT);
                scaleTransitionThread1.start();
            }
            lastScrollTime = currentTime;
        });
    }

    private void initCanvasEvent(){
        // 添加鼠标事件监听器
        canvas.setOnMousePressed(event -> {
            offsetX = event.getX();
            offsetY = event.getY();
        });

        canvas.setOnMouseDragged(event -> {
            // 更新图片位置
            dx = event.getX() - offsetX;
            dy = event.getY() - offsetY;
            // 重绘图片
            drawPicture(canvas, image, 1+factor, dx, dy,0);
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

        public ScaleTransitionThread(double startFactor,double endFactor){
            this.startFactor = startFactor;
            this.endFactor = endFactor;
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
                    drawPicture(canvas,image,1+startFactor,0,0,0);
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
                    drawPicture(canvas,image,1+startFactor,0,0,0);
                    try {
                        sleep(15);
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

