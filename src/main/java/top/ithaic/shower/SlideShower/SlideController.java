package top.ithaic.shower.SlideShower;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    public Button leftRotatePicture;
    @FXML
    public Button rightRotatePicture;
    @FXML
    public Button compressImage;
    @FXML
    public Pane compressPane;
    @FXML
    public Slider desFileSize;
    @FXML
    public Label sizeShower;
    @FXML
    public CheckBox saveAs;
    @FXML
    public Button compress;
    @FXML
    public Label compressResult;
    @FXML
    public Button close;
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
        new SlideShower(pictureShower);
        new SlideListener(pictureShower,slidePlay,toolBar,mainPane,pictureScanner,pictureShower,compressPane,lastPicture,nextPicture,amplifyPicture,shrinkPicture,leftRotatePicture,rightRotatePicture,compressImage);//监听pane右键点击与幻灯片播放
        new CompressListener(pictureShower,compressPane,desFileSize,sizeShower,saveAs,compress,compressResult,close);
    }

}
