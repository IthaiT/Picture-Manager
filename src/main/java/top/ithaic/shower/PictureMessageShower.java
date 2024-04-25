package top.ithaic.shower;

import javafx.scene.control.TextField;
import top.ithaic.utils.FileMessageUtil;
import top.ithaic.utils.FilePathUtil;

public class PictureMessageShower {
    private static TextField pictureMessage;
    public PictureMessageShower(){
    }
    public PictureMessageShower(TextField pictureMessage){
        PictureMessageShower.pictureMessage = pictureMessage;
        PictureMessageShower.pictureMessage.setEditable(false);
    }

    public void updateText(){
        updateText(0);
    }
    public void updateText(int num){
        String text = FileMessageUtil.countPictureNumber(FilePathUtil.getCurrentFiles()) + "张图片";
        double length = FileMessageUtil.countPictureSize(FilePathUtil.getCurrentFiles());
        String[] units = {"B","KB","MB","GB"};
        int count = 0;
        while(length >= 1024){
            length = length / 1024;
            count++;
        }
        text += "(" + String.format("%.2f",length) + units[count] +")-选中"+ num +"张图片";
        pictureMessage.setText(text);
    }

}
