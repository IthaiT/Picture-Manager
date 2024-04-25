package top.ithaic.utils;

import top.ithaic.shower.PictureShower;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;


public final class PictureSorterUtil {

    private static  File[] pictures;
    private static final PictureShower pictureShower = new PictureShower();

    //根据名字进行分类
    public static void sortWithName() {
        pictures = PictureUtil.getPicturesInDirectory(FilePathUtil.getCurrentFiles());
        Arrays.sort(pictures, Comparator.comparing(File::getName));
        pictureShower.showPicture(pictures);
    }
    //根据大小进行分类
    public static void sortWithSize(){
        pictures = PictureUtil.getPicturesInDirectory(FilePathUtil.getCurrentFiles());
        Arrays.sort(pictures, Comparator.comparing(File::length));
        pictureShower.showPicture(pictures);
    }

    //根据上次修改的时间进行分类
    public static void sortWithLastModify(){
        pictures = PictureUtil.getPicturesInDirectory(FilePathUtil.getCurrentFiles());
        Arrays.sort(pictures, Comparator.comparing(File::lastModified));
        pictureShower.showPicture(pictures);
    }

}
