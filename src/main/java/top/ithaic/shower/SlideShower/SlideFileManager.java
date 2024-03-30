package top.ithaic.shower.SlideShower;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import top.ithaic.imageview.Thumbnail;

import java.io.File;
import java.util.ArrayList;

public final class SlideFileManager{
    private static IntegerProperty scannerIndex;
    private static int currentIndex;
    private static File[] pictures;
    public SlideFileManager(File[] pictures,int currentIndex){
        SlideFileManager.pictures = pictures;
        SlideFileManager.currentIndex = currentIndex;
        SlideFileManager.scannerIndex = new SimpleIntegerProperty(0);
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

    public static int getScannerIndex() {
        return scannerIndex.get();
    }

    public static IntegerProperty scannerIndexProperty() {
        return scannerIndex;
    }

    public static void setScannerIndex(int scannerIndex) {
        SlideFileManager.scannerIndex.set(scannerIndex);
    }
}
