package top.ithaic.utils;

import javafx.scene.control.Button;

import java.io.File;

public final class  PictureUtil {
    public boolean isPicture(File file){
        String[] formats = {".jpg",".jpeg",".bmp",".gif",".png"};
        boolean judge = false;
        for(String format :formats){
            if(file.getName().toLowerCase().endsWith(format))
                judge = true;
        }
        return judge;
    }
}
