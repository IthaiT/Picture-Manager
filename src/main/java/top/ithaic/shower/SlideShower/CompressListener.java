package top.ithaic.shower.SlideShower;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import top.ithaic.Myinterface.Listener;
import top.ithaic.utils.PictureOperationUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class CompressListener implements Listener {
    private static Pane pictureShower;
    private static Pane compressPane;
    private static Slider desFileSize;
    private static Label sizeShower;
    private static CheckBox saveAs;
    private static Button compress;
    private static Label compressResult;
    private static Button close;
    public CompressListener(Pane pictureShower,Pane compressPane,Slider desFileSize, Label sizeShower, CheckBox saveAs, Button compress, Label compressResult, Button close){
        CompressListener.pictureShower = pictureShower;
        CompressListener.compressPane = compressPane;
        CompressListener.desFileSize = desFileSize;
        CompressListener.sizeShower = sizeShower;
        CompressListener.saveAs  = saveAs;
        CompressListener.compress = compress;
        CompressListener.compressResult = compressResult;
        CompressListener.close = close;
        Listen();
        startTimer();
    }

    @Override
    public void Listen(){
        close.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> pictureShower.getChildren().remove(compressPane));

        sizeShower.setText(" %");

        compressResult.setText("----");

        desFileSize.setMin(0.5);
        desFileSize.setMax(1);
        desFileSize.setOnScroll(mouseEvent->{
            desFileSize.setValue(desFileSize.getValue()+mouseEvent.getDeltaY()/100*0.2);
        });
        desFileSize.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            String text = String.format("%.2f",newValue.doubleValue()*100);
            sizeShower.setText(text+" %");
        });

        File sourceImage = SlideFileManager.getPictures()[SlideFileManager.getCurrentIndex()];
        compress.setOnMouseClicked(mouseEvent -> {
            if(PictureOperationUtil.compressImage(sourceImage,desFileSize.getValue(),saveAs.isSelected()))compressResult.setText("压缩成功");
            else compressResult.setText("压缩失败");
        });
    }

    private void startTimer(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    if(compressResult.getText().compareTo("----")!=0){
                        compressResult.setText("----");
                    }
                });
            }
        };
        timer.schedule(task,0,10000);
    }
}
