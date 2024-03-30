package top.ithaic.utils;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.listener.PictureOperateListener;
import top.ithaic.listener.PictureShowerListener;
import top.ithaic.shower.PictureShower;
import top.ithaic.shower.SlideShower.SlideFileManager;
import top.ithaic.shower.SlideShower.SlideShower;
import top.ithaic.shower.SlideShower.SlideWindow;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;


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
        for(int i =0 ;i <thumbnails.size();i++){
            //如果不存在，发出警告
            if(!thumbnails.get(i).getImageFile().exists()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(thumbnails.get(i).getImageFile() + "不存在，请确认该项目的位置");
                alert.show();
                thumbnails.clear();
            }
        }

        //得到当前目录下所有图片，为判断是否文件名冲突
        List<File> imageFiles = Arrays.stream(currentPath.listFiles()).filter(file -> PictureUtil.isPicture(file)).toList();

        //将名字没有冲突的文件直接粘贴，名字有冲突的文件过滤出来
        ArrayList<File> conflictFiles = new ArrayList<>();
        for (Thumbnail thumbnail : thumbnails) {
            String sourceName = thumbnail.getImageFile().getName();
            String targetName = null;
            for(File file : File.listRoots()){
                if(currentPath.equals(file)){
                    targetName = currentPath + sourceName;
                    break;
                }
                targetName = currentPath.toPath() + "\\" + sourceName;
            }
            //如果粘贴的位置是本地文件夹，那么直接创建副本
            if(thumbnail.getImageFile().toString().equals(targetName)){
                System.out.println("同名文件");
                Path sourcePath = thumbnail.getImageFile().toPath();
                Path targetPath = thumbnail.getImageFile().toPath();
                while(isNameExit(sourceName, imageFiles)){
                    targetName = sourceName.replaceFirst( "(?=\\.[^.]+$)", "-副本");
                    targetPath = targetPath.resolveSibling(targetName);
                    sourceName = targetName;
                }
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                imageFiles = Arrays.stream(currentPath.listFiles()).filter(file -> PictureUtil.isPicture(file)).toList();
                continue;
            }
            //如果粘贴位置是其他文件夹，且有同名文件，将其加入冲突文件列表中
            if(isNameExit(sourceName,imageFiles)){
                conflictFiles.add(thumbnail.getImageFile());
                continue;
            }
            //如果没有相同名字的文件，粘贴后刷新
            Files.copy(Path.of(thumbnail.getImageFile().toString()), Path.of(currentPath.toPath() +"/"+ sourceName));
        }
        if(conflictFiles.isEmpty()){
            pictureShower.showPicture(currentPath);
            return;
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(StageManager.getStageStack().peek());
        stage.setResizable(false);
        stage.setWidth(440);
        stage.setHeight(280);
        stage.setTitle("替换或跳过文件");

        Label info1 = new Label("正在将"+thumbnails.size() + "个项目从 " + thumbnails.get(0).getImageFile().getParentFile().getName() +  " 复制到 " + currentPath.getName());
        Label info2 = new Label("目标包含"+ conflictFiles.size() + "个同名文件");
        Label replaceLabel = new Label(  "✔ 替换目标中的文件");
        Label ignoreLabel = new Label("❌ 跳过这些文件");
        info1.setStyle("  -fx-font-size: 12px; -fx-pref-width: 400px; -fx-pref-height: 25px;");
        info2.setStyle("  -fx-font-size: 16px; -fx-pref-width: 400px; -fx-pref-height: 50px;");
        replaceLabel.setStyle("  -fx-font-size: 16px; -fx-pref-width: 400px; -fx-pref-height: 50px;");
        ignoreLabel.setStyle("-fx-font-size: 16px; -fx-pref-width: 400px; -fx-pref-height: 50px;");
        VBox vBox = new VBox();
        vBox.getChildren().addAll(info1,info2,replaceLabel,ignoreLabel);
        StackPane stackPane = new StackPane(vBox);
        stackPane.setPrefWidth(440);
        stackPane.setPrefHeight(280);
        stackPane.setStyle("-fx-background-color:  rgb(243,243,243);");
        vBox.setMaxHeight(200);
        vBox.setMaxWidth(400);
        stackPane.setAlignment(Pos.CENTER);

        replaceLabel.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount()>=1){
                try {
                    for(File file : conflictFiles){
                        Files.copy(Path.of(file.toURI()), Path.of(currentPath.toPath() +"/"+ file.getName()), StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.close();
                pictureShower.showPicture(currentPath);
            }
        });
        replaceLabel.setOnMouseEntered(mouseEvent -> {
            replaceLabel.setStyle("  -fx-font-size: 16px; -fx-pref-width: 400px; -fx-pref-height: 50px;-fx-background-color:  rgb(204,232,247);");
        });
        replaceLabel.setOnMouseExited(mouseEvent -> {
            replaceLabel.setStyle("  -fx-font-size: 16px; -fx-pref-width: 400px; -fx-pref-height: 50px;-fx-background-color:  rgb(243,243,243);");

        });
        ignoreLabel.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() >= 1) {
                    stage.close();
                    pictureShower.showPicture(currentPath);
                    System.out.println("测试点击ignore");
                }
            });
        ignoreLabel.setOnMouseEntered(mouseEvent -> {
            ignoreLabel.setStyle("  -fx-font-size: 16px; -fx-pref-width: 400px; -fx-pref-height: 50px;-fx-background-color:  rgb(204,232,247);");
        });
        ignoreLabel.setOnMouseExited(mouseEvent -> {
            ignoreLabel.setStyle("  -fx-font-size: 16px; -fx-pref-width: 400px; -fx-pref-height: 50px;-fx-background-color:  rgb(243,243,243);");
        });
        stage.setScene(new Scene(stackPane));
        stage.show();
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
            return;
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
            return;
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
        if(PictureShowerListener.getSlideWindow()!=null){
            PathUtil.updateFiles();
            SlideFileManager.setPictures(PathUtil.getCurrentFiles());
            if(SlideFileManager.getCurrentIndex()==SlideFileManager.getPictures().length)
                SlideFileManager.setCurrentIndex(SlideFileManager.getCurrentIndex()-1);
            new SlideShower().drawPicture();
        }
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
