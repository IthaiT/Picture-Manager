package top.ithaic.listener;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import top.ithaic.Myinterface.Listener;
import top.ithaic.utils.ImageSearchUtil;
import top.ithaic.utils.PathUtil;

import java.io.File;


public class SearchButtonListener implements Listener {
    private Button searchButton;
    private TextField searchName;

    public SearchButtonListener(TextField searchName,Button searchButton){
        this.searchButton = searchButton;
        this.searchName = searchName;
        Listen();
    }
    @Override
    public void Listen(){
        this.searchButton.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            if(this.searchName.getText().isEmpty())return;
            //创建ImageSearchUtil对象搜索
            System.out.println("搜索文件名为:"+this.searchName.getText());
            File[] searchResult;

            try {
                new ImageSearchUtil().search(PathUtil.getCurrentPath(),this.searchName.getText());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
