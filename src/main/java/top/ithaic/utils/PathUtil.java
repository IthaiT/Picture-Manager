package top.ithaic.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public final class PathUtil {
    private static List<String> historyPath;
    private static File currentPath;
    private static File[] currentFiles;
    private static StringProperty currentPathProperty;
    private static File lastPath;

    static {
        currentPathProperty = new SimpleStringProperty("");
        historyPath = new ArrayList<>();
        currentPath = null;
        lastPath = null;
        currentFiles = null;
    }
    public static void updatePath(File newPath){
        //路径相同 无需更新
        if(newPath == currentPath)
        {
            //刷新相同路径，也得更改属性
            currentPathProperty.setValue(currentPathProperty.getValue()+" ");
            return;
        }

        //路径更新
        currentPathProperty.setValue(newPath.getAbsolutePath());
        lastPath = currentPath;
        currentPath = newPath;

        //历史路径更新
        if(historyPath!=null && historyPath.size() >= 10)historyPath.remove(0);
        if (historyPath!= null && !historyPath.contains(newPath.getAbsolutePath()))historyPath.add(newPath.getAbsolutePath());

        //刷新图片文件数组
        updateFiles();
    }
    public static void updateFiles(File[] currentFiles) {
        //更新当前文件
        PathUtil.currentFiles = currentFiles;
    }
    public static void updateFiles(){
        PathUtil.updateFiles(PictureUtil.getPicturesInDirectory(currentPath));
    }

    public static File[] getCurrentFiles() {return currentFiles;}
    public static File getCurrentPath() {
        return currentPath;
    }
    public static File getLastPath() {
        return lastPath;
    }
    public static List<String> getHistoryPath() {
        return historyPath;
    }
    public static StringProperty getCurrentPathProperty() {
        return currentPathProperty;
    }
}
