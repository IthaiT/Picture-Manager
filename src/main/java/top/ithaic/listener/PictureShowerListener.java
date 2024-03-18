package top.ithaic.listener;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.shower.PictureMessageShower;
import top.ithaic.shower.SlideShower.SlideWindow;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class PictureShowerListener implements Listener {
    private EventHandler<MouseEvent> mousePressEventHandler;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mouseReleasedEventHandler;
    private EventHandler<MouseEvent> mouseClickEventHandler;
    private EventHandler<MouseEvent> autoScrollTimer;//当鼠标到底部时触发滚动
    private static FlowPane thumbnails;
    private static ScrollPane scrollPane;
    private static ArrayList<Thumbnail> thumbnailArrayList = new ArrayList<>();
    private final Timer timer = new Timer();
    private final Rectangle rectangle;
    boolean isSingleClick = false;


    private double startX,startY;
    private final PictureMessageShower pms = new PictureMessageShower();


    public PictureShowerListener(FlowPane thumbnails, ScrollPane scrollPane){
        PictureShowerListener.scrollPane = scrollPane;
        PictureShowerListener.thumbnails = thumbnails;
        //创建鼠标拖动矩形
        rectangle = new Rectangle();
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.getStrokeDashArray().addAll(4d,4d);
        rectangle.setStrokeWidth(2);
        rectangle.setVisible(false);
        Listen();
    }
    @Override
    public void Listen() {
        mousePressEventHandler = this::handleMousePressed;
        mouseDraggedEventHandler = this::handleMouseDragged;
        mouseReleasedEventHandler = this::handleMouseReleased;
        mouseClickEventHandler = this::handleMouseClicked;
        autoScrollTimer = this::handleScrollSlide;
        //启动两个基本的鼠标事件
        thumbnails.addEventHandler(MouseEvent.MOUSE_PRESSED,mousePressEventHandler);
    }
    private void handleMousePressed(MouseEvent mouseEvent){
        isSingleClick = false;//防止对鼠标点击事件造成影响
        thumbnails.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickEventHandler);
        if(isClickBlankArea(mouseEvent)) {
            clearSelected();
            //创建一个anchorPane
            thumbnails.prefWidthProperty().bind(scrollPane.widthProperty().subtract(10));
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefWidth(scrollPane.getWidth());
            anchorPane.getChildren().add(thumbnails);
            anchorPane.getChildren().add(rectangle);
            anchorPane.setPrefWidth(scrollPane.getWidth());
            scrollPane.setContent(anchorPane);
            //添加事件处理器
            thumbnails.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickEventHandler);
            thumbnails.addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseDraggedEventHandler);
            thumbnails.addEventHandler(MouseEvent.MOUSE_RELEASED,mouseReleasedEventHandler);
        }
        else{
            thumbnails.removeEventHandler(MouseEvent.MOUSE_DRAGGED,mouseDraggedEventHandler);
            thumbnails.removeEventHandler(MouseEvent.MOUSE_RELEASED,mouseReleasedEventHandler);
        }
        startX = mouseEvent.getX();
        startY = mouseEvent.getY();
        rectangle.setX(startX);
        rectangle.setY(startY);
        rectangle.setWidth(0);
        rectangle.setHeight(0);
        rectangle.setVisible(true);
    }
    private void handleMouseDragged(MouseEvent mouseEvent){
        thumbnails.addEventHandler(MouseEvent.MOUSE_DRAGGED,autoScrollTimer);
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
            if ((node instanceof Thumbnail) && (node.getBoundsInParent().intersects(rectangle.getBoundsInLocal()))) {
                thumbnail.setSelectedStyle();
                thumbnail.setIsClicked(true);
                thumbnailArrayList.add(thumbnail);
            }
        }
        if(thumbnailArrayList!=null) pms.updateText(thumbnailArrayList.size());//更新选中图片的信息
    }
    private void handleMouseReleased(MouseEvent mouseEvent){
        scrollPane.setContent(thumbnails);
        rectangle.setVisible(false);
    }
    private void handleMouseClicked(MouseEvent mouseEvent){
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
        if(mouseEvent.isControlDown()){
            Thumbnail thumbnail;
            for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
                if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                    thumbnail = (Thumbnail) node;
                    //如果曾经被选中，设置为未选中
                    if(thumbnail.getIsClicked()){
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

        //TODO 如果被选中的图片大于1，清空列表
        if(!thumbnailArrayList.isEmpty()&&(thumbnailArrayList.size() != 1)){
            for (Thumbnail thumbnail : thumbnailArrayList){
                thumbnail.setUnSelectedStyle();
                thumbnail.setIsClicked(false);
            }
            thumbnailArrayList.clear();
        }

        //TODO 单击选中图片
        if(mouseEvent.getClickCount() == 1){
            Thumbnail thumbnail;
            for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
                if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                    thumbnail = (Thumbnail)node;
                    if(!thumbnailArrayList.isEmpty()){
                        //如果上次选中的跟这次选中的相同，清除后返回
                        if(thumbnail.equals(thumbnailArrayList.get(0))){
                            isSingleClick = true;
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if(isSingleClick){
                                        thumbnailArrayList.get(0).setIsClicked(false);
                                        thumbnailArrayList.get(0).setUnSelectedStyle();
                                        thumbnailArrayList.clear();
                                    }
                                    isSingleClick = false;
                                }
                            },100);
                            return;
                        }
                        //如果不同，直接清除，不返回
                        else{
                            thumbnailArrayList.get(0).setIsClicked(false);
                            thumbnailArrayList.get(0).setUnSelectedStyle();
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
        //TODO 双击显示幻灯片
        else{
            isSingleClick = false;
            Thumbnail thumbnail;
            for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
                if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                    thumbnail = (Thumbnail)node;
                    if(!thumbnailArrayList.isEmpty()){
                        //如果这次与上次双击的图片相同，创建幻灯片后返回
                        if(thumbnailArrayList.get(0).equals(thumbnail)){
                            System.out.println("点击了相同的文件");
                            new SlideWindow(thumbnailArrayList.get(0).getImageFile());
                            return;
                        }
                        //否则把选中的图片取消
                        else{
                            thumbnailArrayList.get(0).setUnSelectedStyle();
                            thumbnailArrayList.get(0).setIsClicked(false);
                            thumbnailArrayList.clear();
                        }
                    }

                    thumbnailArrayList.add(thumbnail);
                    thumbnail.setIsClicked(true);
                    thumbnail.setSelectedStyle();
                    new SlideWindow(thumbnailArrayList.get(0).getImageFile());
                    pms.updateText(thumbnailArrayList.size());
                    return;
                }
            }
        }

        if(isClickBlankArea(mouseEvent)){
            clearSelected();
        }
    }
    private void handleScrollSlide(MouseEvent mouseEvent){
        double scrollEdgeThreshold = 50;
        double scrollDelta = 0.01;
        double sceneY = mouseEvent.getSceneY() - 117.0;//得到鼠标在scrollPane中的位置
        if(mouseEvent.getY() > thumbnails.getHeight())return;//当鼠标位置超过图片显示区域时，直接返回
        if(sceneY < scrollEdgeThreshold){
            double newVvalue = scrollPane.getVvalue()- scrollDelta;
            scrollPane.setVvalue(newVvalue);
        }
        else if(sceneY > scrollPane.getHeight() - scrollEdgeThreshold){
            double newVvalue = scrollPane.getVvalue() + scrollDelta;
            scrollPane.setVvalue(newVvalue);
        }
    }
    private boolean isClickBlankArea(MouseEvent mouseEvent){
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
            if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                return false;
            }
        }
        return true;
    }
    private void clearSelected(){
        pms.updateText(0);
        if(thumbnailArrayList.isEmpty())return;
        for(Thumbnail thumbnail : thumbnailArrayList){
            thumbnail.setUnSelectedStyle();
            thumbnail.setIsClicked(false);
        }
        thumbnailArrayList.clear();
    }
}
