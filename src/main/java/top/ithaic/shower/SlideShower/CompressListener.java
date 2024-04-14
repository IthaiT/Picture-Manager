package top.ithaic.shower.SlideShower;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import top.ithaic.Myinterface.Listener;



public class CompressListener implements Listener {
    private static BorderPane mainPane;
    private static Pane compressPane;
    private static Slider desFileSize;
    private static Label sizeShower;
    private static CheckBox saveAs;
    private static Button compress;
    private static Label compressResult;
    private static Button close;
    public CompressListener(BorderPane mainPane,Pane compressPane,Slider desFileSize, Label sizeShower, CheckBox saveAs, Button compress, Label compressResult, Button close){
        CompressListener.mainPane = mainPane;
        CompressListener.compressPane = compressPane;
        CompressListener.desFileSize = desFileSize;
        CompressListener.sizeShower = sizeShower;
        CompressListener.saveAs  = saveAs;
        CompressListener.compress = compress;
        CompressListener.compressResult = compressResult;
        CompressListener.close = close;
        Listen();
    }

    @Override
    public void Listen(){
        close.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> mainPane.getChildren().remove(compressPane));
    }
}
