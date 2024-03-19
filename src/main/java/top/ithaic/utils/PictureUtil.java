package top.ithaic.utils;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public final class  PictureUtil {

    public static int getPictureIndex(File picture){
        int index = 0;
        PathUtil.updateFiles();
        for(File file:PathUtil.getCurrentFiles()){
            if(file.equals(picture))return index;
            index++;
        }
        return index;
    }
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
        if(filePath == null) return new File[0];
        File[] files = filePath.listFiles();
        if(files == null) return new File[0];
        List<File> ImageFiles = Arrays.stream(files).filter(file -> isPicture(file)).toList();
        return ImageFiles.toArray(new File[0]);
    }

    public static File[] getPicturesInDirectory(File[] filesPath){
        if(filesPath == null) return new File[0];
        List<File> ImageFiles = Arrays.stream(filesPath).filter(file -> isPicture(file)).toList();
        return ImageFiles.toArray(new File[0]);
    }
}
