package top.ithaic.listener;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import top.ithaic.HelloApplication;
import top.ithaic.Myinterface.Listener;
import top.ithaic.imageview.Thumbnail;
import top.ithaic.shower.PictureShower;
import top.ithaic.utils.PictureOperationUtil;
import top.ithaic.utils.StageManager;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuBarListener implements Listener {
    private static MenuBar menuBar;
    private static final Menu fileMenu = new Menu("文件");
    private static final MenuItem openMenu = new MenuItem("打开文件夹");
    private static final MenuItem closeMenu = new MenuItem("关闭");
    private static final Menu editMenu = new Menu("编辑");
    private static final MenuItem renameMenu = new MenuItem("重命名");
    private static final MenuItem copyMenu = new MenuItem("复制");
    private static final MenuItem pasteMenu = new MenuItem("粘贴");
    private static final MenuItem deleteMenu = new MenuItem("删除");

    private static final Menu helpMenu = new Menu("帮助");
    private static final MenuItem contactMenu = new MenuItem("联系我们");
    private static ArrayList<Thumbnail> thumbnailArrayList;

    public MenuBarListener(){}
    public MenuBarListener(MenuBar menuBar){
        MenuBarListener.menuBar = menuBar;
        MenuBarListener.menuBar.getMenus().addAll(fileMenu,editMenu,helpMenu);
        fileMenu.getItems().addAll(openMenu,closeMenu);
        editMenu.getItems().addAll(renameMenu,copyMenu,pasteMenu,deleteMenu);
        helpMenu.getItems().addAll(contactMenu);
        thumbnailArrayList = PictureShowerListener.getThumbnailArrayList();
        Listen();
    }

    @Override
    public void Listen() {
//        menuBar.setOnMouseClicked(mmouseEvent -> {
//            if(mmouseEvent.getClickCount() >= 1){
//                if(thumbnailArrayList == null || thumbnailArrayList.isEmpty()) {
//                    renameMenu.setDisable(true);
//                    copyMenu.setDisable(true);
//                    pasteMenu.setDisable(true);
//                    deleteMenu.setDisable(true);
//                }else{
//                    renameMenu.setDisable(false);
//                    copyMenu.setDisable(false);
//                    pasteMenu.setDisable(false);
//                    deleteMenu.setDisable(false);
//                }
//            }
//        });
        openMenu.setOnAction(actionEvent -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("选择文件夹");

            File selectedFolder = directoryChooser.showDialog(StageManager.getNowStage());
            PictureShower pictureShower = new PictureShower();
            pictureShower.showPicture(selectedFolder);

        });
        closeMenu.setOnAction(actionEvent -> {
            Stage mainStage = StageManager.getNowStage();
            mainStage.close();
        });
        renameMenu.setOnAction(actionEvent -> {
            if(thumbnailArrayList.isEmpty())return;
            PictureOperationUtil.renamePictures();
        });
        copyMenu.setOnAction(actionEvent -> {
            PictureOperationUtil.copyPictures();
        });
        pasteMenu.setOnAction(actionEvent -> {
            try {
                PictureOperationUtil.pastePictures();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        deleteMenu.setOnAction(actionEvent -> {
            if(thumbnailArrayList.isEmpty())return;
            for(Thumbnail thumbnail : thumbnailArrayList){
                if(!thumbnail.getIsClicked())return;
            }
            try {
                PictureOperationUtil.deletePictures();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        contactMenu.setOnAction(actionEvent -> {
            Image image = new Image(Objects.requireNonNull(MenuBarListener.class.getResourceAsStream("/top/ithaic/icons/contact.jpg")));

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200); // 设置图片宽度
            imageView.setPreserveRatio(true); // 保持图片比例

            // 创建一段文字
            Text text = new Text("Phone:18826241122");
            text.setFont(Font.font("Arial", 20));
            text.setFill(Color.BLACK);

            // 将图片和文字放在水平布局中
            HBox hbox = new HBox(10); // 设置间距
            StackPane imageStackPane = new StackPane(imageView);
            imageStackPane.setPrefWidth(200);
            imageStackPane.setPrefHeight(200);
            StackPane textStackPane = new StackPane(text);
            textStackPane.setPrefWidth(200);
            textStackPane.setPrefHeight(200);
            hbox.getChildren().addAll(imageStackPane,textStackPane);

            // 设置背景颜色
            StackPane root = new StackPane();
            root.setStyle("-fx-background-color: mistyrose;");
            root.getChildren().add(hbox);

            Scene scene = new Scene(root, 400, 200);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // 设置为模态窗口
            stage.setScene(scene);
            stage.setResizable(false); // 设置窗口大小不可改变
            Image icon = new Image(String.valueOf(HelloApplication.class.getResource("/top/ithaic/icons/softIcon.png")));
            stage.getIcons().add(icon);
            stage.show();
        });

    }
}
