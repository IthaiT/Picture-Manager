package top.ithaic.shower.SlideShower;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import top.ithaic.imageview.Thumbnail;

import java.io.File;
import java.util.ArrayList;

public final class SlideFileManager{
    private static IntegerProperty currentIndexProperty;
    private static IntegerProperty picturesLengthProperty;
    private static int currentIndex;
    private static File[] pictures;
    public SlideFileManager(File[] pictures,int currentIndex){
        SlideFileManager.pictures = pictures;
        SlideFileManager.currentIndex = currentIndex;
        SlideFileManager.currentIndexProperty = new SimpleIntegerProperty(currentIndex);
        SlideFileManager.picturesLengthProperty = new SimpleIntegerProperty(pictures.length);
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

    public static IntegerProperty currentIndexPropertyProperty() {
        return currentIndexProperty;
    }
    public static void setCurrentIndexProperty(int currentIndexProperty) {
        SlideFileManager.currentIndexProperty.set(currentIndexProperty);
    }

    public static IntegerProperty picturesLengthProperty() {
        return picturesLengthProperty;
    }
    public static void setPicturesLengthProperty(int picturesLengthProperty) {
        SlideFileManager.picturesLengthProperty.set(picturesLengthProperty);
    }
}
