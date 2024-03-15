package top.ithaic.utils;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
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
        if(files == null) return null;
        List<File> ImageFiles = Arrays.stream(files).filter(file -> isPicture(file)).toList();
        return ImageFiles.toArray(new File[0]);
    }

    public static File[] getPicturesInDirectory(File[] filesPath){
        if(filesPath == null) return new File[0];
        List<File> ImageFiles = Arrays.stream(filesPath).filter(file -> isPicture(file)).toList();
        return ImageFiles.toArray(new File[0]);
    }
}
