package top.ithaic.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class PathUtil {
    private static StringProperty currentPathProperty;
    private static File currentPath;
    private static File lastPath;

    static {
        currentPathProperty = new SimpleStringProperty("");
        currentPath = null;
        lastPath = null;
    }
    public static void updatePath(File newPath){
        //路径相同 无需更新
        if(newPath == currentPath) return;
        //路径更新
        currentPathProperty.setValue(newPath.getAbsolutePath());
        lastPath = currentPath;
        currentPath = newPath;
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
