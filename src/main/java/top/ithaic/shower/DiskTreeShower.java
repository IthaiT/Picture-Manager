package top.ithaic.shower;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/*
*   TODO
*    还需要修改的地方
*    1、根目录不要加上展开图标
*    2、加上磁盘图标于文件夹图标
*
* */
public class DiskTreeShower {
    private static final Image DISK_IMAGE = new Image(Objects.requireNonNull(DiskTreeShower.class.getResourceAsStream("/top/ithaic/icons/partition.png")));
    private static final Image FOLDER_IMAGE = new Image(Objects.requireNonNull(DiskTreeShower.class.getResourceAsStream("/top/ithaic/icons/directory.png")));
    private static final Image COMPUTER_IMAGE = new Image(Objects.requireNonNull(DiskTreeShower.class.getResourceAsStream("/top/ithaic/icons/computer.png")));
    private static final Image DOCUMENT_IMAGE = new Image(Objects.requireNonNull(DiskTreeShower.class.getResourceAsStream("/top/ithaic/icons/document.png")));
    private static final Image PICTURES_IMAGE = new Image(Objects.requireNonNull(DiskTreeShower.class.getResourceAsStream("/top/ithaic/icons/pictures.png")));
    private static final Image DESKTOP_IMAGE = new Image(Objects.requireNonNull(DiskTreeShower.class.getResourceAsStream("/top/ithaic/icons/desktop.png")));
    private ImageView getIcon(Image image){
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        return imageView;
    }

    public class MyFile{
        private File file;
        private String filename;

        public MyFile(File file){
            this.file = file;
            this.filename = file.getName();
            if(this.filename.compareTo("Documents")==0)this.filename = "文档";
            else if(this.filename.compareTo("Pictures")==0)this.filename = "图片";
            else if(this.filename.compareTo("Desktop")==0)this.filename = "桌面";
        }
        public MyFile(File file,String relativeFilename){
            this.file = file;
            this.filename = relativeFilename;
        }

        public File getFile() {
            return file;
        }
        public void setFile(File file) {
            this.file = file;
        }
        @Override
        public String toString(){
            return this.filename;
        }
    }
    //TODO 构建目录树
    public DiskTreeShower(TreeView disktree){
        MyFile documentFile = new MyFile(new File(System.getenv("USERPROFILE") + "\\Documents"));
        MyFile picturesFile = new MyFile(new File(System.getenv("USERPROFILE") + "\\Pictures"));
        MyFile desktopFile = new MyFile(new File(System.getenv("USERPROFILE") + "\\Desktop"));

        TreeItem<MyFile> virtualRoot = new TreeItem<>();

        TreeItem<MyFile> computerItem = new TreeItem<>(new MyFile(new File("此电脑")),getIcon(COMPUTER_IMAGE));
        virtualRoot.getChildren().addAll(computerItem,createNode(desktopFile),createNode(picturesFile),createNode(documentFile));

        //设置树视图的根目录
        disktree.setRoot(virtualRoot);
        //获取电脑磁盘分区
        ArrayList<MyFile> diskPartitions = getDiskPartitions();
        if (diskPartitions != null) {
            for(MyFile partition : diskPartitions){
                //递归获取每一个分区的目录
                TreeItem<MyFile> item = createNode(partition);
                item.setGraphic(getIcon(DISK_IMAGE));
                //将分区添加到“此电脑”节点
                TreeItem<MyFile> temp = (TreeItem<MyFile>) disktree.getTreeItem(0).getChildren().get(0);
                temp.getChildren().add(item);
            }
        }
        disktree.setShowRoot(false);
    }

    // TODO 获取电脑磁盘分区
    private ArrayList<MyFile> getDiskPartitions(){
        ArrayList<MyFile> diskPartitions = new ArrayList<>();
        //得到电脑所有磁盘
        File[] disks = File.listRoots();
        if(disks == null){
            return null;
        }
        //将磁盘添加到线性表中
        for(File partition : disks){
            diskPartitions.add(new MyFile(partition, partition.getPath()));
        }
        return diskPartitions;
    }

    private TreeItem<MyFile> createNode(MyFile myFile){
        Image Icon = FOLDER_IMAGE;
        if(myFile.filename.compareTo("文档")==0)Icon=DOCUMENT_IMAGE;
        else if(myFile.filename.compareTo("桌面")==0)Icon=DESKTOP_IMAGE;
        else if(myFile.filename.compareTo("图片")==0)Icon=PICTURES_IMAGE;
        return new TreeItem<>(myFile,getIcon(Icon)){
            private boolean isLeaf;
            /*
            *  以下两个变量保证每次展开目录时不会重复计算
            * */
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;
            @Override
            public ObservableList<TreeItem<MyFile>> getChildren(){
                // 重新构建父节点的列表
               if(isFirstTimeChildren) {
                   isFirstTimeChildren = false;
                   super.getChildren().setAll(buildChildren());
               }
                // 返回父节点的列表
                return super.getChildren();
            }

            @Override
            public boolean isLeaf(){
                if(isFirstTimeLeaf){
                    isFirstTimeLeaf = false;
                    MyFile f = getValue();
                    isLeaf = f.getFile().isFile() ;
                }
                return isLeaf;
            }

            //文件过滤器
            private File[] getDirectory(File dir){
                return dir.listFiles(File::isDirectory);
            }

            // 递归构建子目录
            private ObservableList<TreeItem<MyFile>> buildChildren(){
                if(myFile.getFile() == null){
                    return FXCollections.emptyObservableList();
                }
                //如果是文件，返回空列表
                if(myFile.getFile().isFile()){
                    return FXCollections.emptyObservableList();
                }
                File[] files = getDirectory(myFile.getFile());
                if(files != null){
                    ObservableList<TreeItem<MyFile>> children = FXCollections.observableArrayList();
                    for(File f : files){
                        if(f.isHidden())continue;  //隐藏文件不加入集合
                        children.add(createNode(new MyFile(f)));
                    }
                    return children;
                }
                return FXCollections.emptyObservableList();
            }
        };
    }

}
