package top.ithaic.listener;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.shower.PictureShower;
import top.ithaic.utils.FilePathUtil;

import java.util.Objects;

public class CommonListener implements Listener {
    private static Button backward;
    private static Button forward;
    private static Slider sizeChanger;

    public CommonListener(Slider sizeChanger, Button...buttons){
        backward = buttons[0];
        forward = buttons[1];
        CommonListener.sizeChanger = sizeChanger;

        //设置后退按钮图标
        ImageView image = new ImageView(new Image(Objects.requireNonNull(CommonListener.class.getResourceAsStream("/top/ithaic/icons/back.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(backward.getPrefWidth());
        image.setFitHeight(backward.getPrefHeight());
        backward.setGraphic(image);
        //设置前进按钮图标
        image = new ImageView(new Image(Objects.requireNonNull(CommonListener.class.getResourceAsStream("/top/ithaic/icons/forward.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(forward.getPrefWidth());
        image.setFitHeight(forward.getPrefHeight());
        forward.setGraphic(image);

        Listen();
    }
    @Override
    public void Listen(){
        PictureShower pictureShower = new PictureShower();

        backward.setOnMouseClicked(mouseEvent -> {
            if(FilePathUtil.getCurrentPath() !=null)
                pictureShower.showPicture(FilePathUtil.getCurrentPath().getParentFile());
        });

        forward.setOnMouseClicked(mouseEvent -> {
            if(FilePathUtil.getLastPath() !=null&& FilePathUtil.getLastPath().getParentFile()!=null&& FilePathUtil.getLastPath().getParentFile().compareTo(FilePathUtil.getCurrentPath())==0)
                pictureShower.showPicture(FilePathUtil.getLastPath());
        });


        //首先初始化Slider参数
        sizeChanger.setMin(60);
        sizeChanger.setMax(200);
        sizeChanger.setValue(100);

        //监听Slider的改变
        sizeChanger.valueProperty().addListener(((observableValue, oldValue, newValue) -> {
            pictureShower.showPicture();
            Thumbnail thumbnail = new Thumbnail();
            double newWidth = thumbnail.getThumbnailWidth() * ((newValue.doubleValue())/ oldValue.doubleValue());
            double newHeight = thumbnail.getThumbnailHeight() * (newValue.doubleValue() /oldValue.doubleValue());
            thumbnail.setThumbnailWidth(newWidth);
            thumbnail.setThumbnailHeight(newHeight);
        }));
        sizeChanger.setOnScroll(mouseEvent->{
            sizeChanger.setValue(sizeChanger.getValue()+mouseEvent.getDeltaY()*0.2);
        });
    }
}

