package top.ithaic.listener;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.Thumbnail;


public class PictureShowerListener implements Listener {
    private static FlowPane thumbnnails;
    private static boolean singlePictureSelected = false;//是否有图片被选中

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
            /*
            * TODO 分为两种效果
            *   第一种 点中空白区域
            *   第二种 点中图片区域
            * */


            //如果没有图片被选中，设置图片边框
            if (!singlePictureSelected) {
                for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
                    if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                        ((Thumbnail) node).setSelectedStyle();
                        singlePictureSelected = true;
                        selectedNode = node;
                        return;
                    }
                }
            }
            /*
            * 如果有图片被选中
            * 判断此次点击的图片是不是跟上次相同
            * 如果是，那么取消选择
            * 如果不是，取消原来图片的选中效果，设置新的被选中图片选中效果
            * */
            else {
                for (Node node : ((FlowPane) mouseEvent.getSource()).getChildren()) {
                    if ((node instanceof Thumbnail) && (node.getBoundsInParent().contains(mouseX, mouseY))) {
                        if (!selectedNode.equals(node)) {
                            ((Thumbnail) selectedNode).setUnSelectedStyle();
                            selectedNode = node;
                            ((Thumbnail) selectedNode).setSelectedStyle();
                            singlePictureSelected = true;
                            System.out.println("一次点击");
                            return;
                        }
                    }
                }
                singlePictureSelected = false;
                ((Thumbnail) selectedNode).setUnSelectedStyle();
            }
        });
    }
}
