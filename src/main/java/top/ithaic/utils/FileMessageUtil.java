package top.ithaic.utils;

import java.io.File;

public final class FileMessageUtil {

    public static int countPictureNumber(File[] files){
        if(files==null) return 0;
        return PictureUtil.getPicturesInDirectory(files).length;
    }
    public static long countPictureSize(File[] files){
        if(files==null) return 0;
        long sumSize = 0;
        File[] pictureFiles = PictureUtil.getPicturesInDirectory(files);
        for(File file:pictureFiles)sumSize+=file.length();
        return sumSize;
    }
}
