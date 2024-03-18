package top.ithaic.utils;

import top.ithaic.imageview.Thumbnail;
import top.ithaic.listener.PictureShowerListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class PictureOperationUtil {
    private static ArrayList<Thumbnail> thumbnails;
    public static void copyPictures(){
        thumbnails = PictureShowerListener.getThumbnailArrayList();
    }
    public static void pastePictures(){
        File currentPath = PathUtil.getCurrentPath();
        if(!currentPath.exists()) return;
        Path destDir = currentPath.toPath();
        for(Thumbnail thumbnail : thumbnails){
            try {
                Files.copy(thumbnail.getImageFile().toPath(), destDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void renamePictures(){

    }
    public static void deletePictures(){

    }
}
