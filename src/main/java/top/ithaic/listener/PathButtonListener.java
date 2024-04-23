package top.ithaic.listener;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import top.ithaic.Myinterface.Listener;
import top.ithaic.shower.DiskTreeShower;
import top.ithaic.shower.PictureShower;
import top.ithaic.utils.PathUtil;

import java.util.Objects;

public class PathButtonListener implements Listener {
    private static Button backward;
    private static Button forward;

    public PathButtonListener(Button...buttons){
        PathButtonListener.backward = buttons[0];
        PathButtonListener.forward = buttons[1];
        //设置后退按钮图标
        ImageView image = new ImageView(new Image(Objects.requireNonNull(PathButtonListener.class.getResourceAsStream("/top/ithaic/icons/back.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(backward.getPrefWidth());
        image.setFitHeight(backward.getPrefHeight());
        backward.setGraphic(image);
        //设置前进按钮图标
        image = new ImageView(new Image(Objects.requireNonNull(PathButtonListener.class.getResourceAsStream("/top/ithaic/icons/forward.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(forward.getPrefWidth());
        image.setFitHeight(forward.getPrefHeight());
        forward.setGraphic(image);

        Listen();
    }

    @Override
    public void Listen(){
        PictureShower pictureShower = new PictureShower();

        PathButtonListener.backward.setOnMouseClicked(mouseEvent -> {
            if(PathUtil.getCurrentPath() !=null)
                pictureShower.showPicture(PathUtil.getCurrentPath().getParentFile());
        });

        PathButtonListener.forward.setOnMouseClicked(mouseEvent -> {
            if(PathUtil.getLastPath() !=null&&PathUtil.getLastPath().getParentFile()!=null&&PathUtil.getLastPath().getParentFile().compareTo(PathUtil.getCurrentPath())==0)
                pictureShower.showPicture(PathUtil.getLastPath());
        });
    }
}
