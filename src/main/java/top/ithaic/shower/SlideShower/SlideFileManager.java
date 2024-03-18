package top.ithaic.shower.SlideShower;

import top.ithaic.imageview.Thumbnail;

import java.io.File;
import java.util.ArrayList;

public final class SlideFileManager{
    private static int currentIndex;
    private static File[] pictures;
    public SlideFileManager(File[] pictures,int currentIndex){
        SlideFileManager.pictures = pictures;
        SlideFileManager.currentIndex = currentIndex;
    }

    public static File[] getPictures() {return pictures;}
    public static void setPictures(File[] pictures) {
        SlideFileManager.pictures = pictures;
    }
    public static int getCurrentIndex() {
        return currentIndex;
    }
    public static void setCurrentIndex(int currentIndex) {
        SlideFileManager.currentIndex = currentIndex;
    }
}
