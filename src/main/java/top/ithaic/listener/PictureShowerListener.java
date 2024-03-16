package top.ithaic.listener;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.shower.PictureMessageShower;
import top.ithaic.shower.SlideShower;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class PictureShowerListener implements Listener {
    private static FlowPane thumbnnails;
    private static ArrayList<Thumbnail> thumbnailArrayList = new ArrayList<>();
    private Timer timer = new Timer();
    boolean isSingleClick = false;
    private Rectangle rectangle;

    private double startX,startY;
    private final PictureMessageShower pms = new PictureMessageShower();
    private EventHandler<MouseEvent> mouseReleasedEventHandler;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mouseClick;

    public PictureShowerListener( FlowPane thumbnnails){
        PictureShowerListener.thumbnnails = thumbnnails;
        rectangle = new Rectangle();
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLUE);
        rectangle.setStrokeWidth(2);
        rectangle.setVisible(false);
        Listen();
    }
    @Override
    public void Listen() {
        mouseClick = mouseEvent -> {
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
                                new SlideShower(thumbnailArrayList.get(0).getImageFile());
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
                        new SlideShower(thumbnailArrayList.get(0).getImageFile());
                        pms.updateText(thumbnailArrayList.size());
                        return;
                    }
                }
            }

            if(isClickBlankArea(mouseEvent)){
                clearSelected();
            }
        };

        PictureShowerListener.thumbnnails.setOnMousePressed(this::handleMousePressed);
        mouseDraggedEventHandler = this::handleMouseDragged;
        mouseReleasedEventHandler = this::handleMouseReleased;

    }
    private void handleMousePressed(MouseEvent mouseEvent){
        isSingleClick = false;//防止对点击造成影响
        thumbnnails.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseClick);
        if(isClickBlankArea(mouseEvent)) {
            clearSelected();
            thumbnnails.removeEventHandler(MouseEvent.MOUSE_CLICKED,mouseClick);
            thumbnnails.addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseDraggedEventHandler);
            thumbnnails.addEventHandler(MouseEvent.MOUSE_RELEASED,mouseReleasedEventHandler);
        }
        else{
            thumbnnails.removeEventHandler(MouseEvent.MOUSE_DRAGGED,mouseDraggedEventHandler);
            thumbnnails.removeEventHandler(MouseEvent.MOUSE_RELEASED,mouseReleasedEventHandler);
        }
        startX = mouseEvent.getX();
        startY = mouseEvent.getY();
        rectangle.setX(startX);
        rectangle.setY(startY);
        rectangle.setWidth(0);
        rectangle.setHeight(0);
        rectangle.setVisible(true);
        System.out.println("矩形初始化");
    }
    private void handleMouseDragged(MouseEvent mouseEvent){
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
            if(thumbnail.getIsClicked()) {
                thumbnail.setUnSelectedStyle();
                thumbnail.setIsClicked(false);
                thumbnailArrayList.remove(thumbnail);
            }
            if ((node instanceof Thumbnail) && (node.getBoundsInParent().intersects(rectangle.getBoundsInLocal()))) {
                thumbnail.setSelectedStyle();
                thumbnail.setIsClicked(true);
                thumbnailArrayList.add(thumbnail);
            }
        }
        if(thumbnailArrayList!=null) pms.updateText(thumbnailArrayList.size());//更新选中图片的信息
    }
    private void handleMouseReleased(MouseEvent mouseEvent){
        System.out.println("鼠标松开");
        rectangle.setVisible(false);
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
