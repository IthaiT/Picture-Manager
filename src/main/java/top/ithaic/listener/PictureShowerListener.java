package top.ithaic.listener;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.Thumbnail;


public class PictureShowerListener implements Listener {
    private static FlowPane thumbnnails;
    public PictureShowerListener( FlowPane thumbnnails){
        PictureShowerListener.thumbnnails = thumbnnails;
        Listen();
    }
    @Override
    public void Listen() {
        PictureShowerListener.thumbnnails.setOnMouseClicked(mouseEvent -> {

            double mouseX = mouseEvent.getX();
            double mouseY = mouseEvent.getY();
            Thumbnail[] thumbnails = thumbnnails.getChildren().toArray(new Thumbnail[0]);
            for(Node node : ((FlowPane)mouseEvent.getSource()).getChildren()){
                if(node instanceof Thumbnail){
                    if(node.getBoundsInParent().contains(mouseX,mouseY)){
                        return;
                    }
                }
            }
            for(Thumbnail thumbnail : thumbnails){
                if(thumbnail.getIsClicked()){
                    thumbnail.setUnSelectedStyle();
                    thumbnail.setIsClicked(false);
                }
            }
        });

    }
}
