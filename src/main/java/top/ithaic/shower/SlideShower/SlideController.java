package top.ithaic.shower.SlideShower;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;


public class SlideController {
    @FXML
    private Button shrinkPicture;
    @FXML
    private Button amplifyPicture;
    @FXML
    private Button lastPicture;
    @FXML
    private Button nextPicture;
    @FXML
    private Pane pictureShower;

    @FXML
    private void initialize(){
        new SlideShower(pictureShower,lastPicture,nextPicture,amplifyPicture,shrinkPicture);
        new SlideListener(pictureShower);
    }

}
