package top.ithaic.shower.SlideShower;

import java.io.File;

public final class SlideFileManager{
    private static File picture;

    public SlideFileManager(File picture){
        SlideFileManager.picture = picture;
    }

    public static File getPicture() {
        return picture;
    }
}
