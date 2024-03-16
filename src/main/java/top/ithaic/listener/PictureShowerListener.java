package top.ithaic.listener;

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
import java.util.concurrent.TimeUnit;


public class PictureShowerListener implements Listener {
    private static FlowPane thumbnnails;
    private static ArrayList<Thumbnail> thumbnailArrayList = new ArrayList<>();
    private Timer timer = new Timer();
    boolean isSingleClick = false;
    boolean isBlankArea = true;
    private Rectangle rectangle;

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
        PictureMessageShower pms = new PictureMessageShower();
        PictureShowerListener.thumbnnails.setOnMouseClicked(mouseEvent -> {
            isBlankArea = true;
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
                        isBlankArea = false;
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
                        isBlankArea = false;
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
                        isBlankArea = false;
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

            if(isBlankArea){
                pms.updateText(0);
                if(thumbnailArrayList.isEmpty())return;
                for(Thumbnail thumbnail : thumbnailArrayList){
                    thumbnail.setUnSelectedStyle();
                    thumbnail.setIsClicked(false);
                }
                thumbnailArrayList.clear();
            }
        });


//        PictureShowerListener.thumbnnails.setOnMousePressed(mouseEvent -> {
//            System.out.println("鼠标按下");
//            System.out.println(mouseEvent.getX());
//            System.out.println(mouseEvent.getY());
//            rectangle.setX(mouseEvent.getX());
//            rectangle.setY(mouseEvent.getY());
//            System.out.println(rectangle.getX());
//            System.out.println(rectangle.getY());
//            rectangle.setWidth(100);
//            rectangle.setHeight(100);
//            rectangle.setVisible(true);
//        });
//        PictureShowerListener.thumbnnails.setOnMouseDragged(mouseEvent -> {
//            System.out.println("鼠标拖动");
//            double width = mouseEvent.getX() - rectangle.getWidth();
//            double height = mouseEvent.getY() - rectangle.getHeight();
//            rectangle.setWidth(Math.abs(width));
//            rectangle.setHeight(Math.abs(height));
//            System.out.println(rectangle.getWidth());
//            thumbnnails.getChildren().add(rectangle);
//        });
//        PictureShowerListener.thumbnnails.setOnMouseReleased(mouseEvent -> {
//            System.out.println("鼠标松开");
//
//            rectangle.setVisible(false);
//        });
    }
}
