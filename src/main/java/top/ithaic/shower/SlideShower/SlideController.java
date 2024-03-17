package top.ithaic.shower.SlideShower;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SlideController {

    @FXML
    private Pane pictureShower;
    @FXML
    private void initialize(){
        new SlideShower(pictureShower);
    }

}
