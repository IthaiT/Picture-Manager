package top.ithaic.shower;

import javafx.scene.control.TextField;
import top.ithaic.utils.PictureUtil;

import java.io.File;

public class PictureMessageShower {
    private static TextField pictureMessage;

    public PictureMessageShower(){
    }
    public PictureMessageShower(TextField pictureMessage){
        PictureMessageShower.pictureMessage = pictureMessage;
    }

    private int countPictureNumber(){
        int count = 0;
        PictureUtil pictureUtil = new PictureUtil();
        File selectedPath = PictureShower.getCurrentPath();
        if(selectedPath == null) return 0;
        File[] files = selectedPath.listFiles();
        if (files == null) return 0;
        for(File file : files){
            if(pictureUtil.isPicture(file))
                count++;
        }
        return count;
    }
    private long countPictureSize(){
        long count = 0;
        PictureUtil pictureUtil = new PictureUtil();
        File selectedPath = PictureShower.getCurrentPath();
        if(selectedPath == null) return 0;
        File[] files = selectedPath.listFiles();
        if (files == null) return 0;
        for(File file : files){
            if(pictureUtil.isPicture(file))
                count += file.length();
        }
        return count;
    }

    public void updateText(){
        String text = countPictureNumber() + "张图片";
        double length = countPictureSize();
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
