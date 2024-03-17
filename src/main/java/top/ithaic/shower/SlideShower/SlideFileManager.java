package top.ithaic.shower.SlideShower;

import java.io.File;

public final class SlideFileManager{
    private static File currentPicture;
    private static File[] pictures;
    public SlideFileManager(File currentPicture){
        SlideFileManager.currentPicture = currentPicture;
    }
    public SlideFileManager(File[] pictures){SlideFileManager.pictures = pictures;}

    public static File getCurrentPicture() {
        return currentPicture;
    }
}
