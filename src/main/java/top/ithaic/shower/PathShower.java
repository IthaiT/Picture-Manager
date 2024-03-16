package top.ithaic.shower;

import javafx.beans.binding.StringBinding;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import top.ithaic.utils.PathUtil;

import java.io.File;


public class PathShower {
    private  static  ContextMenu historyPath;
    private  static TextField pathShower;
    private  static AnchorPane anchorPane;
    public PathShower(){}
    public PathShower(TextField pathShower, AnchorPane father, ContextMenu historyPath){
        PathShower.pathShower = pathShower;
        PathShower.anchorPane = father;
        PathShower.historyPath = historyPath;
        bindProperty();
        addListener();
    }
    public void addListener(){
        PictureShower pictureShower = new PictureShower();

        //历史路径追踪
        PathShower.pathShower.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount() >= 2 && mouseEvent.getButton() == MouseButton.PRIMARY){
                PathShower.historyPath.getItems().clear();
                if(PathUtil.getHistoryPath() == null) return;

                for(String path:PathUtil.getHistoryPath()){
                    MenuItem menuItem = new MenuItem(path);
                    PathShower.historyPath.getItems().add(menuItem);
                }
                double screenX = pathShower.getScene().getWindow().getX() + pathShower.getLayoutBounds().getWidth();
                double screenY = pathShower.getScene().getWindow().getY() + pathShower.getLayoutBounds().getHeight();
                PathShower.historyPath.show(PathShower.pathShower.getScene().getWindow(),screenX,screenY);
            }
        });

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
