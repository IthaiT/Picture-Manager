package top.ithaic.listener;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.shower.PictureMessageShower;
import top.ithaic.shower.PictureShower;
import top.ithaic.shower.SlideShower.SlideFileManager;
import top.ithaic.shower.SlideShower.SlideShower;
import top.ithaic.shower.SlideShower.SlideWindow;
import top.ithaic.utils.FilePathUtil;
import top.ithaic.utils.PictureOperationUtil;
import top.ithaic.utils.PictureUtil;


import java.io.File;
import java.io.IOException;
import java.util.*;

import static javafx.scene.paint.Color.rgb;


public class PictureShowerListener implements Listener {
    private EventHandler<MouseEvent> mousePressEventHandler;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mouseReleasedEventHandler;
    private EventHandler<MouseEvent> mouseClickEventHandler;
    private EventHandler<MouseEvent> autoScrollTimer;//当鼠标到底部时触发滚动
    private static FlowPane thumbnails;
    private static StackPane noPicturePane;
    private static ScrollPane scrollPane;
    public final static Image NO_PICTURE = new Image(Objects.requireNonNull(PictureShowerListener.class.getResourceAsStream("/top/ithaic/icons/noResult.png")));
    private static ArrayList<Thumbnail> thumbnailArrayList;
    private static ContextMenu contextMenuT;//On Thumbnail
    private static ContextMenu contextMenuP;//On Pane
    private static SlideWindow slideWindow;
    private static TextField textField = null;
    private final Timer timer = new Timer();
    private Rectangle rectangle;
    boolean isSingleClick = false;
    private double startX, startY;
    private final PictureMessageShower pms = new PictureMessageShower();
    private String preName = null;
    private String newName = null;
    private boolean preClick = false;
    private boolean mouseIsClick = false;


    public PictureShowerListener(FlowPane thumbnails, ScrollPane scrollPane) {
        //程序起始时，没有任何图片
        ImageView imageView = new ImageView(NO_PICTURE);
        noPicturePane = new StackPane(imageView);
        imageView.setFitHeight(500);
        imageView.setPreserveRatio(true);
        noPicturePane.setPrefWidth(scrollPane.getWidth());
        scrollPane.setContent(noPicturePane);

        PictureShowerListener.scrollPane = scrollPane;
        PictureShowerListener.thumbnails = thumbnails;
        thumbnails.prefWidthProperty().bind(scrollPane.widthProperty().subtract(10));
        thumbnailArrayList = new ArrayList<>();
        //初始化右键点击菜单
        contextMenuT = new ContextMenu();//for thumbnail
        contextMenuP = new ContextMenu();//for pane
        contextMenuT.setStyle(" -fx-background-color: white");
        contextMenuP.setStyle(" -fx-background-color: white");
        new PictureOperateListener(contextMenuT,contextMenuP);
        //创建鼠标拖动矩形
        rectangle = new Rectangle();
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.getStrokeDashArray().addAll(4d, 4d);
        rectangle.setStrokeWidth(2);
        rectangle.setVisible(false);
        Listen();
    }

    @Override
    public void Listen(){
        mousePressEventHandler = this::handleMousePressed;
        mouseDraggedEventHandler = this::handleMouseDragged;
        mouseReleasedEventHandler = this::handleMouseReleased;
        mouseClickEventHandler = this::handleMouseClicked;
        autoScrollTimer = this::handleScrollSlide;

        EventHandler<KeyEvent> keyCtrlPress = this::handleCtrlPressed;
        EventHandler<MouseEvent> noPictureEvent = this::handleNoPicture;
        //启动基本的鼠标事件
        thumbnails.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressEventHandler);
        noPicturePane.addEventHandler(MouseEvent.MOUSE_CLICKED,noPictureEvent);
        scrollPane.addEventHandler(KeyEvent.KEY_PRESSED,keyCtrlPress);
        scrollPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
               handleNameChange();
                preClick = false;
            }
        });
    }

    private void handleMousePressed(MouseEvent mouseEvent) {
        contextMenuP.hide();
        isSingleClick = false;//防止对鼠标点击事件造成影响
        thumbnails.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickEventHandler);
        handleNameChange();//处理点击label重命名
        if (isClickBlankArea(mouseEvent)) {
            mouseIsClick = true;
            if(mouseEvent.getButton() == MouseButton.SECONDARY){
                contextMenuP.show(scrollPane,mouseEvent.getScreenX(),mouseEvent.getScreenY());
            }
            clearSelected();
            //创建一个anchorPane
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefWidth(scrollPane.getWidth());
            anchorPane.getChildren().add(thumbnails);
            anchorPane.getChildren().add(rectangle);
            anchorPane.setStyle("-fx-background-color: #f0f4fa;");
            scrollPane.setContent(anchorPane);
            //添加事件处理器
            thumbnails.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedEventHandler);
            thumbnails.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleasedEventHandler);
            startX = mouseEvent.getX();
            startY = mouseEvent.getY();
            rectangle.setX(startX);
            rectangle.setY(startY);
            rectangle.setWidth(0);
            rectangle.setHeight(0);
            rectangle.setVisible(true);
        } else{
            if(mouseIsClick)return;
            thumbnails.removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedEventHandler);
            thumbnails.removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleasedEventHandler);
        }
    }

    private void handleMouseDragged(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.SECONDARY)return;
        thumbnails.addEventHandler(MouseEvent.MOUSE_DRAGGED, autoScrollTimer);
        double endX = mouseEvent.getX();
        double endY = mouseEvent.getY();
        double width = endX - startX;
        double height = endY - startY;
        rectangle.setWidth(Math.abs(width));
        rectangle.setHeight(Math.abs(height));
        rectangle.setX(width < 0 ? endX : startX);
        rectangle.setY(height < 0 ? endY : startY);
        Thumbnail thumbnail;
        for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
            thumbnail = (Thumbnail) node;
            if (thumbnail.getIsClicked()) {
                thumbnail.setIsClicked(false);
                thumbnail.setUnSelectedStyle();
                thumbnailArrayList.remove(thumbnail);
            }
            if ((node instanceof Thumbnail) && (node.getBoundsInParent().intersects(rectangle.getBoundsInLocal()))) {
                thumbnail.setSelectedStyle();
                thumbnail.setIsClicked(true);
                thumbnailArrayList.add(thumbnail);
            }
        }
        if (thumbnailArrayList != null) pms.updateText(thumbnailArrayList.size());//更新选中图片的信息
    }

    private void handleMouseReleased(MouseEvent mouseEvent) {
        scrollPane.setContent(thumbnails);
        rectangle.setVisible(false);
        mouseIsClick = false;
    }

    private void handleMouseClicked(MouseEvent mouseEvent) {
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        /*
         * 1、ctrl+鼠标单击
         * （1）选中图片：若已经被选中图片，取消选中，若未被选中，设置为选中
         * （2）若无ctrl+鼠标单击事件，将界面中被选中的图片全部清除
         * 2、鼠标点击
         * （1）点击时会等待100ms看看会不会有下一次点击
         * （2）如果没有
         *       当这次选中图片与上次相同时，取消选中
         *       当这次选中图片与上次不同时，取消上次选中，选中这次点击的图片
         * （3）如果双击
         *       将点击的图片设置为选中，不管上次是否被选中，取消未被选中图片，显示幻灯片
         *       通过singalClick变量忽略单击事件
         * 3、点中空白区域
         *   将所有选中图片的取消
         * */
        //TODO ctrl按下选中多张图片
        if (mouseEvent.isControlDown()) {
            Thumbnail thumbnail;
            for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
                if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                    thumbnail = (Thumbnail) node;
                    //如果曾经被选中，设置为未选中
                    if (thumbnail.getIsClicked()) {
                        thumbnail.setUnSelectedStyle();
                        thumbnail.setIsClicked(false);
                        thumbnailArrayList.remove(thumbnail);
                        pms.updateText(thumbnailArrayList.size());//更新被选中的图片
                        continue;
                    }
                    thumbnailArrayList.add(thumbnail);
                    thumbnail.setSelectedStyle();
                    thumbnail.setIsClicked(true);
                }
            }
            pms.updateText(thumbnailArrayList.size());
            return;//下面的代码都不需要执行
        }

        //判断是不是右击事件
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            if(thumbnailArrayList.size()>=2){
                for(Thumbnail thumbnail : thumbnailArrayList){
                    thumbnail.setIsClicked(false);
                    if(thumbnail.getBoundsInParent().contains(mouseX,mouseY)){
                        contextMenuT.show(thumbnail,mouseEvent.getScreenX(),mouseEvent.getScreenY());
                        return;
                    }
                }
            }
            Thumbnail thumbnail = getClickedThumbnail(mouseEvent);
            if(!thumbnailArrayList.isEmpty()){
                clearSelected();
            }
            if(thumbnail==null)return;
            contextMenuT.show(thumbnail,mouseEvent.getScreenX(),mouseEvent.getScreenY());
            thumbnail.setSelectedStyle();
            thumbnail.setIsClicked(true);
            thumbnailArrayList.add(thumbnail);
            pms.updateText(thumbnailArrayList.size());
            return;
        }

        contextMenuT.hide();
        //TODO 如果被选中的图片大于1，清空列表
        if (!thumbnailArrayList.isEmpty() && (thumbnailArrayList.size() != 1)) {
            for (Thumbnail thumbnail : thumbnailArrayList) {
                thumbnail.setUnSelectedStyle();
                thumbnail.setIsClicked(false);
            }
            thumbnailArrayList.clear();
        }

        //TODO 单击选中图片
        if (mouseEvent.getClickCount() == 1) {
            Thumbnail thumbnail;
            //鼠标左击事件
            for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
                if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                    thumbnail = (Thumbnail) node;
                    if (!thumbnailArrayList.isEmpty()) {
                        //如果上次选中的跟这次选中的相同并且标签没有被点中，清除后返回
                        if (thumbnail.equals(thumbnailArrayList.get(0)) && !thumbnail.getIsLabelClicked()) {
                            if(preClick){
                                preClick =false;
                                return;
                            }
                            isSingleClick = true;
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (isSingleClick) {
                                        clearSelected();
                                    }
                                    isSingleClick = false;
                                }
                            }, 100);
                            return;
                        }
                        //如果标签被点中
                        else if(thumbnail.getIsClicked()){
                            handleLabelClicked(thumbnail);
                            return;
                        }
                        //如果不同，直接清除，不返回
                        else {
                            thumbnailArrayList.get(0).setIsClicked(false);
                            thumbnailArrayList.get(0).setUnSelectedStyle();
                            thumbnailArrayList.clear();
                        }
                    }
                    thumbnail.setUnSelectedStyle();
                    thumbnailArrayList.add(thumbnail);
                    thumbnail.setIsClicked(true);
                    thumbnail.setSelectedStyle();
                    pms.updateText(thumbnailArrayList.size());
                    return;
                }
            }
        }
        //TODO 双击显示幻灯片
        else {
            isSingleClick = false;
            Thumbnail thumbnail;
            for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
                if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                    thumbnail = (Thumbnail) node;
                    if (!thumbnailArrayList.isEmpty()) {
                        //如果这次与上次双击的图片相同，创建幻灯片后返回
                        if (thumbnailArrayList.get(0).equals(thumbnail)) {
                            if(slideWindow==null) slideWindow = new SlideWindow(FilePathUtil.getCurrentFiles(), PictureUtil.getPictureIndex(thumbnailArrayList.get(0).getImageFile()));
                            else{
                                SlideFileManager.setCurrentIndex(PictureUtil.getPictureIndex(thumbnailArrayList.get(0).getImageFile()));
                                new SlideShower().drawPicture();
                            }
                            return;
                        }
                        //否则把选中的图片取消
                        else {
                            thumbnailArrayList.get(0).setUnSelectedStyle();
                            thumbnailArrayList.get(0).setIsClicked(false);
                            thumbnailArrayList.clear();
                        }
                    }

                    thumbnailArrayList.add(thumbnail);
                    thumbnail.setIsClicked(true);
                    thumbnail.setSelectedStyle();
                    pms.updateText(thumbnailArrayList.size());
                    return;
                }
            }
        }

        if (isClickBlankArea(mouseEvent)) {
            clearSelected();
        }
    }

    private void handleScrollSlide(MouseEvent mouseEvent) {
        double scrollEdgeThreshold = 50;
        double scrollDelta = 0.01;
        double sceneY = mouseEvent.getSceneY() - 117.0;//得到鼠标在scrollPane中的位置
        if (mouseEvent.getY() > thumbnails.getHeight()) return;//当鼠标位置超过图片显示区域时，直接返回
        if (sceneY < scrollEdgeThreshold) {
            double newVvalue = scrollPane.getVvalue() - scrollDelta;
            scrollPane.setVvalue(newVvalue);
        } else if (sceneY > scrollPane.getHeight() - scrollEdgeThreshold) {
            double newVvalue = scrollPane.getVvalue() + scrollDelta;
            scrollPane.setVvalue(newVvalue);
        }
    }

    private void handleCtrlPressed(KeyEvent keyEvent) {
        KeyCodeCombination keyCodeCombinationC = new KeyCodeCombination(KeyCode.C,KeyCodeCombination.CONTROL_DOWN);
        KeyCodeCombination keyCodeCombinationV = new KeyCodeCombination(KeyCode.V,KeyCodeCombination.CONTROL_DOWN);
        KeyCodeCombination keyCodeCombinationA = new KeyCodeCombination(KeyCode.A,KeyCodeCombination.CONTROL_DOWN);
        if(keyCodeCombinationC.match(keyEvent)){
            PictureOperationUtil.copyPictures();
        }
        if(keyCodeCombinationV.match(keyEvent)){
            try {
                PictureOperationUtil.pastePictures();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(keyEvent.getCode().equals(KeyCode.DELETE)){
            try {
                PictureOperationUtil.deletePictures();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(keyCodeCombinationA.match(keyEvent)){
            PictureOperationUtil.selectAll();
        }
    }
    private void handleNoPicture(MouseEvent mouseEvent){
        if(mouseEvent.getButton() == MouseButton.SECONDARY){
            contextMenuP.show(scrollPane,mouseEvent.getScreenX(),mouseEvent.getScreenY());
            return;
        }
        contextMenuP.hide();
    }
    private void handleLabelClicked(Thumbnail thumbnail){
        Label label = thumbnail.getLabel();
        if(thumbnail.getIsLabelClicked()){
            label.setBackground(new Background(new BackgroundFill(rgb(247, 247, 247), null, null)));

            StackPane stackPane = new StackPane();

            String filename = label.getText();
            preName = filename.substring(0,filename.lastIndexOf("."));

            textField = new TextField();
            textField.setText(preName);
            textField.setAlignment(Pos.CENTER);
            stackPane.getChildren().add(textField);
            thumbnail.setBottom(stackPane);
            textField.requestFocus();
            textField.selectAll();
            textField.setStyle("    -fx-background-insets: 0;\n" +
                    "-fx-effect: null;\n"+
                    "-fx-border-color: rgb(28,136,203);\n"
                    );
        }
        preClick = true;
    }

    private void handleNameChange(){
        if(thumbnailArrayList.isEmpty())return;
        if(textField == null)return;
        newName = textField.getText();
        Thumbnail thumbnail = thumbnailArrayList.get(0);
        if(!newName.isEmpty() && !newName.equals(preName)){
            Label label = thumbnail.getLabel();
            String filename = label.getText();
            String suffix = filename.substring(filename.lastIndexOf("."));
            File currentPath = FilePathUtil.getCurrentPath();
            List<File>  imageFiles = Arrays.stream(currentPath.listFiles()).filter(file -> PictureUtil.isPicture(file)).toList();
            //判断名字是否存在,if如果存在
            if(PictureOperationUtil.isNameExit(newName+suffix,imageFiles)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText(newName + "已被使用");
                alert.showAndWait();
            }
            else{
                File oldFile = thumbnail.getImageFile();
                oldFile.renameTo(new File(oldFile.getParentFile() + "/" + newName + suffix));
                label.setText(oldFile.getName());
                PictureShower pictureShower = new PictureShower();
                pictureShower.showPicture();
            }
        }
        thumbnail.setBottom(thumbnail.getLabel());
        thumbnail.getLabel().setTextFill(Color.BLACK);
        thumbnail.getLabel().setBackground(new Background(new BackgroundFill(rgb(28, 136, 203), null, null)));
        thumbnail.setIsLabelClicked(false);
        textField = null;
    }

    private boolean isClickBlankArea(MouseEvent mouseEvent) {
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
            if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                return false;
            }
        }
        return true;
    }

    private void clearSelected() {
        if(preClick){
            preClick = false;
            return;
        }
        pms.updateText(0);
        if (thumbnailArrayList.isEmpty()) return;
        for (Thumbnail thumbnail : thumbnailArrayList) {
            thumbnail.setUnSelectedStyle();
            thumbnail.setIsClicked(false);
            if(thumbnail.getIsLabelClicked()){
                thumbnail.setUnSelectedStyle();
            }
        }
        thumbnailArrayList.clear();
    }

    private Thumbnail getClickedThumbnail(MouseEvent mouseEvent) {
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
            if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                return (Thumbnail) node;
            }
        }
        return null;
    }
    public static ArrayList<Thumbnail> getThumbnailArrayList(){
        return thumbnailArrayList;
    }

    public static SlideWindow getSlideWindow() {
        return slideWindow;
    }
    public static void setSlideWindow(SlideWindow slideWindow) {
        PictureShowerListener.slideWindow = slideWindow;
    }
    public static StackPane getNoPicturePane(){
        return noPicturePane;
    }

}
