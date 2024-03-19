package top.ithaic.utils;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.listener.PictureOperateListener;
import top.ithaic.listener.PictureShowerListener;
import top.ithaic.shower.PictureShower;
import top.ithaic.shower.SlideShower.SlideFileManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.awt.Desktop;



public class PictureOperationUtil {
    private static ArrayList<Thumbnail> thumbnails = new ArrayList<>();//被复制的图片，存储以方便粘贴
    private static PictureShower pictureShower = new PictureShower();
    public static void copyPictures(){
        thumbnails.clear();
        thumbnails.addAll(PictureShowerListener.getThumbnailArrayList());
    }
    public static void copyPictures(int currentIndex){
        thumbnails.clear();
        thumbnails.add(new Thumbnail(SlideFileManager.getPictures()[currentIndex]));
    }

    public static void pastePictures() throws IOException {
        File currentPath = PathUtil.getCurrentPath();
        if (currentPath == null) return;
        if (!currentPath.exists()) return;
        if (thumbnails.isEmpty()) return;//如果没有文件被复制，返回

        //判断已经复制的文件有没有被修改（删除或者重命名）
        for(Thumbnail thumbnail : thumbnails){
            //如果不存在，发出警告
            if(!thumbnail.getImageFile().exists()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(thumbnail.getImageFile() + "不存在，请确认该项目的位置");
                alert.show();
                thumbnails.clear();
            }
        }

        //得到当前目录下所有图片，为判断是否文件名冲突
        List<File> imageFiles = Arrays.stream(currentPath.listFiles()).filter(file -> PictureUtil.isPicture(file)).toList();
        for (Thumbnail thumbnail : thumbnails) {
            boolean flag = false;
            String sourceName = thumbnail.getImageFile().getName();
            //如果名字冲突，一直要求重命名直到不冲突
            while(isNameExit(sourceName,imageFiles)){
                sourceName = renameSource(thumbnail.getImageFile().toString());
                if(sourceName == null){
                    flag = true;
                    break;
                }
            }
            //没有输入任何名字，直接跳过这个图片的复制
            if(flag)continue;
            Files.copy(Path.of(thumbnail.getImageFile().toString()), Path.of(currentPath.toPath() +"/"+ sourceName));
        }
        pictureShower.showPicture(currentPath);
    }

    public static void renamePictures(){
        ArrayList<Thumbnail> thumbnailArrayList = PictureShowerListener.getThumbnailArrayList();
        if(thumbnailArrayList.size() == 1){
            File oldFile = thumbnailArrayList.get(0).getImageFile();
            String pictureName = oldFile.getName();
            String suffix = pictureName.substring(pictureName.lastIndexOf("."));
            TextInputDialog dialog = new TextInputDialog("defalut");
            dialog.setTitle("文件重命名");
            dialog.setContentText("请输入新的文件名");
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                String newName = result.get()+suffix;
                oldFile.renameTo(new File(oldFile.getParentFile()+"/" +newName));
            }
            pictureShower.showPicture(PathUtil.getCurrentPath());
            return;
        }
        //得到前缀
        String prefix;
        TextInputDialog prefixDialog = new TextInputDialog("default");
        prefixDialog.setTitle("设置文件前缀");
        prefixDialog.setContentText("请输入文件前缀");
        Optional<String> prefixResult = prefixDialog.showAndWait();
        if(prefixResult.isPresent()){
            prefix = prefixResult.get();
        }
        else return;

        System.out.println("前缀" + prefix);

        //得到起始编号
        String code;
        int startCode;
        TextInputDialog startCodeDialog = new TextInputDialog("0");
        startCodeDialog.setTitle("设置起始编号");
        startCodeDialog.setContentText("请输入起始编号");
        Optional<String> startCodeResult = startCodeDialog.showAndWait();
        if(startCodeResult.isPresent()){
            code= startCodeResult.get();
            startCode = Integer.parseInt(code);
        }
        else return;
        //判断起始编号是否合法
        if(startCode<0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("非法输入");
            alert.show();
        }
        System.out.println("起始编号" + startCode);

        //得到编号位数
        int digit;
        String digitS;
        TextInputDialog  digitDialog= new TextInputDialog("4");
        digitDialog.setTitle("设置位数");
        digitDialog.setContentText("请输入你想编排的位数");
        Optional<String> digitResult = digitDialog.showAndWait();
        if(digitResult.isPresent()){
            digitS= digitResult.get();
            digit = Integer.parseInt(digitS);
        }
        else return;
        //判断位数是否足够
        if(digit > 16){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("非法输入");
            alert.show();
        }
        long baseNumber = (long)Math.pow(10,digit);
        if(thumbnailArrayList.size() > baseNumber - startCode){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("编号不足");
            alert.show();
        }

        System.out.println("位数" + digit);
        for (int i = 0; i < thumbnailArrayList.size(); i++) {
            File oldFile = thumbnailArrayList.get(i).getImageFile();
            String pictureName = oldFile.getName();
            String suffix = pictureName.substring(pictureName.lastIndexOf("."));
            long tmp = startCode + i;
            String newName = prefix + String.valueOf(0).repeat(digit-String.valueOf(tmp).length()) + tmp + suffix;
            oldFile.renameTo(new File(oldFile.getParentFile()+"/"+newName));
        }
        pictureShower.showPicture(PathUtil.getCurrentPath());
    }

    public static void renamePictures(int currentIndex){
        File oldFile = SlideFileManager.getPictures()[currentIndex];
        String pictureName = oldFile.getName();
        String suffix = pictureName.substring(pictureName.lastIndexOf("."));
        TextInputDialog dialog = new TextInputDialog("defalut");
        dialog.setTitle("文件重命名");
        dialog.setContentText("请输入新的文件名");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            String newName = result.get()+suffix;
            oldFile.renameTo(new File(oldFile.getParentFile()+"/" +newName));
        }
        pictureShower.showPicture(PathUtil.getCurrentPath());
    }
    //TODO 删除图片，为主窗口使用
    public static void deletePictures() throws IOException {
        ArrayList<Thumbnail> tmp = PictureShowerListener.getThumbnailArrayList();
        if(tmp.isEmpty())return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("确认删除？");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            for (Thumbnail thumbnail : tmp){
                Desktop.getDesktop().moveToTrash(thumbnail.getImageFile());
            }
        }
        pictureShower.showPicture(PathUtil.getCurrentPath());
    }

    //TODO 删除图片，为幻灯片窗口使用
    public static void deletePictures(int currentIndex) throws IOException{
        if(currentIndex<0 || currentIndex > SlideFileManager.getPictures().length)return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("确认删除？");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            Desktop.getDesktop().moveToTrash(SlideFileManager.getPictures()[currentIndex]);
        }
        pictureShower.showPicture(PathUtil.getCurrentPath());
    }

    //TODO 选中主窗口所有图片
    public static void selectAll(){
        FlowPane flowPane = PictureShower.getThumbnails();
        ArrayList<Thumbnail> thumbnailArrayList = PictureShowerListener.getThumbnailArrayList();
        for(Thumbnail thumbnail: thumbnailArrayList){
            thumbnail.setUnSelectedStyle();
            thumbnail.setIsClicked(false);
        }
        thumbnailArrayList.clear();
        for(Node node : flowPane.getChildren()){
            Thumbnail thumbnail = (Thumbnail)node;
            thumbnail.setSelectedStyle();
            thumbnail.setIsClicked(true);
            thumbnailArrayList.add(thumbnail);
        }
    }

    //TODO 重命名被粘贴图片的私有函数
    private static String renameSource(String sourceName){
        //得到图片后缀
        String suffix = sourceName.substring(sourceName.lastIndexOf("."));
        TextInputDialog dialog = new TextInputDialog("default");
        dialog.setTitle("文件名冲突");
        dialog.setHeaderText("目标文件夹中已存在同名文件");
        dialog.setContentText("请输入新的文件名");
        Optional<String> result = dialog.showAndWait();
        return result.map(s -> s + suffix).orElse(null);
    }

    //TODO 判断文件名是否在给定文件中存在
    private static boolean isNameExit(String sourceName,List<File> Dir){
        if(Dir.isEmpty())return false;
        String destName;
        for(File picture : Dir){
            destName = picture.getName();
            if(sourceName.equals(destName)){
                return true;
            }
        }
        return false;
    }
}
