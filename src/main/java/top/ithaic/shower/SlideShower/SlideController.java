package top.ithaic.shower.SlideShower;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;


public class SlideController {
    @FXML
    public Label blankFiller;
    @FXML
    public ToolBar toolBar;
    @FXML
    public BorderPane mainPane;
    @FXML
    public FlowPane pictureScanner;
    @FXML
    private Button shrinkPicture;
    @FXML
    private Button amplifyPicture;
    @FXML
    private Button lastPicture;
    @FXML
    private Button nextPicture;
    @FXML
    private Button slidePlay;//幻灯片播放按钮
    @FXML
    private Pane pictureShower;


    @FXML
    private void initialize(){
        new SlideShower(pictureShower,lastPicture,nextPicture,amplifyPicture,shrinkPicture,slidePlay);
        new SlideListener(pictureShower,slidePlay,toolBar,mainPane,pictureScanner);//监听pane右键点击与幻灯片播放
    }

}
