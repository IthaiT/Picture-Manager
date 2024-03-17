package top.ithaic.shower.SlideShower;

import java.io.File;

public final class SlideFileManager{
    private static File picture;
    private static File[] pictures;
    public SlideFileManager(File picture){
        SlideFileManager.picture = picture;
    }
    public SlideFileManager(File[] pictures){SlideFileManager.pictures = pictures;}

    public static File getPicture() {
        return picture;
    }
}
