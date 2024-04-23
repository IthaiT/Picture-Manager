package top.ithaic.listener;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import top.ithaic.Myinterface.Listener;
import top.ithaic.utils.ImageSearchUtil;
import top.ithaic.utils.PathUtil;

import java.util.Objects;


public class SearchButtonListener implements Listener {
    private Button searchButton;
    private TextField searchName;

    public SearchButtonListener(TextField searchName,Button searchButton){
        this.searchButton = searchButton;
        this.searchName = searchName;
        //设置搜索按钮图标
        ImageView image = new ImageView(new Image(Objects.requireNonNull(PathButtonListener.class.getResourceAsStream("/top/ithaic/icons/search.png"))));
        image.setPreserveRatio(true);
        image.setFitWidth(searchButton.getPrefWidth());
        image.setFitHeight(searchButton.getPrefHeight());
        searchButton.setGraphic(image);

        Listen();
    }


    @Override
    public void Listen(){
        this.searchButton.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            if(this.searchName.getText().isEmpty())return;
            //创建ImageSearchUtil对象搜索
            //搜索
            try {
                new ImageSearchUtil().search(PathUtil.getCurrentPath(),this.searchName.getText());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        this.searchName.addEventFilter(KeyEvent.KEY_PRESSED,keyEvent -> {
            if(this.searchName.getText().isEmpty())return;
            if(keyEvent.getCode() == KeyCode.ENTER) {
                //创建ImageSearchUtil对象搜索
                //搜索
                try {
                    new ImageSearchUtil().search(PathUtil.getCurrentPath(),this.searchName.getText());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }
}
