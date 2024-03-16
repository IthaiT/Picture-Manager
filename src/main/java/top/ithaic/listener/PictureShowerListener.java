package top.ithaic.listener;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.shower.SlideShower;


public class PictureShowerListener implements Listener {
    private static FlowPane thumbnnails;
    private static boolean singlePictureSelected = false;//是否有图片被选中
    private static boolean isBlankArea = true;//判断鼠标点击区域是不是空白区域
    private static Node selectedNode = null;//被选中的图片

    public PictureShowerListener( FlowPane thumbnnails){
        PictureShowerListener.thumbnnails = thumbnnails;
        Listen();
    }
    @Override
    public void Listen() {
        PictureShowerListener.thumbnnails.setOnMouseClicked(mouseEvent -> {
            double mouseX = mouseEvent.getX();
            double mouseY = mouseEvent.getY();
            if(mouseEvent.getClickCount() == 1){
                //TODO 如果没有图片被选中，设置图片边框
                if (!singlePictureSelected) {
                    for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
                        if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                            singlePictureSelected = true;
                            selectedNode = node;
                            ((Thumbnail) selectedNode).setSelectedStyle();
                            isBlankArea = false;//点击的不是空白区域
                            return;
                        }
                    }
                }
                /*
                 * TODO 如果有图片被选中
                 *  判断此次点击的图片是不是跟上次相同
                 *  如果是，那么取消选择
                 *  如果不是，取消原来图片的选中效果，设置新的被选中图片选中效果
                 * */
                else {
                    for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
                        if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                            if (!selectedNode.equals(node)) {
                                ((Thumbnail) selectedNode).setUnSelectedStyle();
                                singlePictureSelected = true;
                                selectedNode = node;
                                ((Thumbnail) selectedNode).setSelectedStyle();
                                isBlankArea = false;//点击的不是空白区域
                                return;
                            }
                        }
                    }
                    singlePictureSelected = false;
                    ((Thumbnail) selectedNode).setUnSelectedStyle();
                }
            }
            //TODO 点击图片有bug
            if(mouseEvent.getClickCount() >= 2 && !isBlankArea){
                if(selectedNode == null) return;
                new SlideShower(((Thumbnail)selectedNode).getImageFile());
            }
            isBlankArea = true;

        });
        PictureShowerListener.thumbnnails.setOnMousePressed(mouseEvent -> {


        });
    }
}
