package top.ithaic.shower;

import javafx.scene.control.TextField;
import top.ithaic.utils.FileMessageUtil;
import top.ithaic.utils.PathUtil;

public class PictureMessageShower {
    private static TextField pictureMessage;
    public PictureMessageShower(){
    }
    public PictureMessageShower(TextField pictureMessage){
        PictureMessageShower.pictureMessage = pictureMessage;
    }

    public void updateText(){
        String text = FileMessageUtil.countPictureNumber(PathUtil.getCurrentFiles()) + "张图片";
        double length = FileMessageUtil.countPictureSize(PathUtil.getCurrentFiles());
        String[] units = {"B","KB","MB","GB"};
        int count = 0;
        while(length >= 1024){
            length = length / 1024;
            count++;
        }
        text += "(" + String.format("%.2f",length) + units[count] +")";
        pictureMessage.setText(text);
    }


}
