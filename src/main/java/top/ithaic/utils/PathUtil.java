package top.ithaic.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class PathUtil {
    private static List<String> historyPath;
    private static File[] currentFiles;
    private static StringProperty currentPathProperty;
    private static File currentPath;
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
        if(newPath == currentPath) return;
        //路径更新
        currentPathProperty.setValue(newPath.getAbsolutePath());
        lastPath = currentPath;
        //历史路径更新
        if(historyPath!=null && historyPath.size() >= 10)historyPath.remove(0);
        if (historyPath!= null && !historyPath.contains(newPath.getAbsolutePath()))historyPath.add(newPath.getAbsolutePath());

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
    public static List<String> getHistoryPath() {
        return historyPath;
    }

    public static StringProperty getCurrentPathProperty() {
        return currentPathProperty;
    }
}
