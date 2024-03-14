package top.ithaic.shower;

import javafx.beans.binding.StringBinding;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import top.ithaic.utils.PathUtil;

import java.io.File;


public class PathShower {
    private  static TextField pathShower;
    private  static AnchorPane anchorPane;
    public PathShower(){}
    public PathShower(TextField pathShower,AnchorPane father){
        PathShower.pathShower = pathShower;
        PathShower.anchorPane = father;
        bindProperty();
        addListener();
    }
    public void addListener(){
        PictureShower pictureShower = new PictureShower();

        PathShower.pathShower.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            //双击解绑属性,编辑路径
            if(mouseEvent.getClickCount() == 1){
                PathShower.pathShower.selectAll();
                PathShower.pathShower.textProperty().unbind();
                PathShower.pathShower.addEventFilter(KeyEvent.KEY_PRESSED,keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.ENTER){
                        File inputPath = new File(PathShower.pathShower.getText());
                        //判断路径是否存在
                        if(inputPath.exists()) pictureShower.showPicture(inputPath);
                        else pictureShower.showPicture();
                    }
                });
            }
        });
        PathShower.anchorPane.widthProperty().addListener((observableValue, oldSize, newSize) -> {
            double newWidth = PathShower.anchorPane.getWidth();
            PathShower.pathShower.setPrefWidth(newWidth - 400);
        });
    }

    public void bindProperty(){
        PathShower.pathShower.textProperty().bind(new StringBinding() {
            {
                bind(PathUtil.getCurrentPathProperty());
            }
            @Override
            protected String computeValue() {
                return PathUtil.getCurrentPathProperty().getValue();
            }
        });
    }
}
