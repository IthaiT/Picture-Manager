package top.ithaic.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class PathUtil {
    private static File[] currentFiles;
    private static StringProperty currentPathProperty;
    private static File currentPath;
    private static File lastPath;

    static {
        currentPathProperty = new SimpleStringProperty("");
        currentPath = null;
        lastPath = null;
        currentFiles = null;
    }
    public static void updatePath(File newPath){
        //路径相同 无需更新
        if(newPath == currentPath) return;
        //路径更新
        currentPathProperty.setValue(newPath.getAbsolutePath());
        lastPath = currentPath;
        currentPath = newPath;
        currentFiles = currentPath.listFiles();
    }
    public static void updateFiles(File[] currentFiles) {
        //更新当前文件
        PathUtil.currentFiles = currentFiles;
    }
    public static File[] getCurrentFiles() {
        return currentFiles;
    }
    public static File getCurrentPath() {
        return currentPath;
    }
    public static File getLastPath() {
        return lastPath;
    }

    public static StringProperty getCurrentPathProperty() {
        return currentPathProperty;
    }
}
