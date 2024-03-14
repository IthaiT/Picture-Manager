package top.ithaic.listener;

import javafx.scene.control.Slider;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.shower.PictureShower;

public class SliderListener {

    private static Slider sizeChanger;
    public SliderListener(Slider sizeChanger) {
        SliderListener.sizeChanger = sizeChanger;
        addSizeListener();
    }

    private void addSizeListener(){
        //首先初始化Slider参数
        sizeChanger.setMin(60);
        sizeChanger.setMax(200);
        sizeChanger.setValue(100);
        PictureShower pictureShower = new PictureShower();
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
