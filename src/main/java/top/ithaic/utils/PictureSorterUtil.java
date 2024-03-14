package top.ithaic.utils;

import top.ithaic.shower.PictureShower;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;


public final class PictureSorterUtil {
    private static  File currentPath;

    private static  File[] pictures;

    private static PictureShower pictureShower = new PictureShower();

    //TODO 根据名字进行分类
    public static void sortWithName() {
        currentPath = PathUtil.getCurrentPath();
        pictures = PictureUtil.getPicturesInDirectory(currentPath);
        System.out.println(currentPath);
        if (pictures == null) return;
        Arrays.sort(pictures, Comparator.comparing(File::getName));
        pictureShower.showPicture(pictures);
    }

    //TODO 根据大小进行分类
    public static void sortWithSize(){
        currentPath = PathUtil.getCurrentPath();
        pictures = PictureUtil.getPicturesInDirectory(currentPath);
        if (pictures == null) return;
        Arrays.sort(pictures, Comparator.comparing(File::length));
        pictureShower.showPicture(pictures);
    }

    //TODO 根据上次修改的时间进行分类
    public static void sortWithLastModify(){
        currentPath = PathUtil.getCurrentPath();
        pictures = PictureUtil.getPicturesInDirectory(currentPath);
        if (pictures == null) return;
        Arrays.sort(pictures, Comparator.comparing(File::lastModified));
        pictureShower.showPicture(pictures);
    }

}
