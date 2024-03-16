package top.ithaic.listener;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.Thumbnail;
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

    public PictureShowerListener( FlowPane thumbnnails){
        PictureShowerListener.thumbnnails = thumbnnails;
        Listen();
    }
    @Override
    public void Listen() {

        PictureShowerListener.thumbnnails.setOnMouseClicked(mouseEvent -> {
            isBlankArea = true;
            double mouseX = mouseEvent.getX();
            double mouseY = mouseEvent.getY();
            /*
            * 逻辑上的优先级顺序：
            * ctrl+鼠标单击优先级最高
            * 如果ctrl+点击事件没有触发，将当前界面上所有图片选中清除
            * 再将当前点击的图片设置为选中
            * */

            //TODO ctrl按下选中多张图片
            if(mouseEvent.isControlDown()){
                Thumbnail thumbnail;
                for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
                    if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                        isBlankArea = false;
                        thumbnail = (Thumbnail) node;
                        thumbnailArrayList.add(thumbnail);
                        //如果曾经被选中，设置为未选中
                        if(thumbnail.getIsClicked()){
                            thumbnail.setUnSelectedStyle();
                            thumbnail.setIsClicked(false);
                            continue;
                        }
                        thumbnail.setSelectedStyle();
                        thumbnail.setIsClicked(true);
                    }
                }
                return;//下面的代码都不需要执行
            }

            //如果被选中的图片大于1，清空列表
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
                                },200);
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
                        return;
                    }
                }
            }

            if(isBlankArea){
                if(thumbnailArrayList.isEmpty())return;
                for(Thumbnail thumbnail : thumbnailArrayList){
                    thumbnail.setUnSelectedStyle();
                    thumbnail.setIsClicked(false);
                }
                thumbnailArrayList.clear();
            }


        });

<<<<<<< HEAD

//        PictureShowerListener.thumbnnails.setOnMousePressed(mouseEvent -> {
//
//        });
=======
        });
>>>>>>> 20aef56b1cd40ecc53015a93fbb800bfcd773ffb
    }
}
