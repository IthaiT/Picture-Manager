package top.ithaic.utils;

import javafx.scene.control.Button;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class  PictureUtil {
    public static boolean isPicture(File file){
        String[] formats = {".jpg",".jpeg",".bmp",".gif",".png"};
        boolean judge = false;
        for(String format :formats){
            if(file.getName().toLowerCase().endsWith(format))
                judge = true;
        }
        return judge;
    }

    public static File[] getPicturesInDirectory(File filePath){
        if(filePath == null) return null;
        File[] files = filePath.listFiles();
        if(files == null)return null;
        ArrayList<File> pictureList = new ArrayList<>();
        for(File file : files){
            if(isPicture(file)){
                pictureList.add(file);
            }
        }
        int size = pictureList.size();
        File[] pictures = new File[size];
        for (int i = 0; i < size; i++) {
            pictures[i] = pictureList.get(i);
        }
        return pictures;
    }
}
