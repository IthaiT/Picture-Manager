package top.ithaic.shower.SlideShower;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;


public class SlideController {
    @FXML
    private Pane pictureShower;
    @FXML
    private Button lastPicture;
    @FXML
    private Button nextPicture;

    @FXML
    private void initialize(){
        new SlideShower(pictureShower,lastPicture,nextPicture);
        new SlideListener(pictureShower);
    }

}
