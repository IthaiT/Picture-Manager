package top.ithaic.showpicture;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.ArrayList;

public class ShowDiskTree {
    public ShowDiskTree(TreeView disktree){
        //设置树视图的根目录
        TreeItem<File> root = createNode(new File("此电脑"));
        disktree.setRoot(root);
        //获取电脑磁盘分区
        ArrayList<File> diskPartitions = getDiskPartitions();
        for(File partition : diskPartitions){
            //递归获取每一个分区的目录
            TreeItem<File> item = createNode(partition);
            //将分区添加到“此电脑”根节点
            disktree.getTreeItem(0).getChildren().add(item);
        }

    }

    //TODO 获取电脑磁盘分区
    private ArrayList<File> getDiskPartitions(){
        ArrayList<File> diskPartitions = new ArrayList<>();
        File[] disks = File.listRoots();
        if(disks == null){
            return null;
        }
        for(File partition : disks){
            diskPartitions.add(partition);
        }
        return diskPartitions;
    }

    private TreeItem<File> createNode(File file){
        return new TreeItem<>(file){
            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;
            @Override
            public ObservableList<TreeItem<File>> getChildren(){
                if(isFirstTimeChildren){
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildchildren(this));
                }
                return super.getChildren();
            }
            @Override
            public boolean isLeaf(){
                if(isFirstTimeLeaf){
                    isFirstTimeLeaf = false;
                    File f = (File)getValue();
                    isLeaf = f.isFile();
                }
                return isLeaf;
            }
            private ObservableList<TreeItem<File>> buildchildren(TreeItem<File> treeItem){
                File f = treeItem.getValue();

                if(f == null){
                    return FXCollections.emptyObservableList();
                }
                if(!f.isDirectory() || f.isHidden()){
                    return FXCollections.emptyObservableList();
                }
                File[] files = f.listFiles();
                if(files != null){
                    ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();
                    for(File childFile : files){
                        children.add(createNode(childFile));
                    }
                    return children;
                }
                return FXCollections.emptyObservableList();
            }
        };
    }

}
