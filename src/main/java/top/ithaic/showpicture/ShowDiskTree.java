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
        disktree.setRoot(new TreeItem(new File("此电脑")));
        //获取电脑磁盘分区
        ArrayList<File> diskPartitions = getDiskPartitions();
        for(File partition : diskPartitions){
            //递归获取每一个分区的目录
            TreeItem<File> item = createNode(partition);
            //将分区添加到“此电脑”根节点
            disktree.getTreeItem(0).getChildren().add(item);
            System.out.println(partition.toString());
        }

    }

    // TODO 获取电脑磁盘分区
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
            /*
            *  以下两个变量保证每次展开目录时不会重复计算
            * */
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;
            @Override
            public ObservableList<TreeItem<File>> getChildren(){
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
                    File f = getValue() ;
                    isLeaf = f.isFile() ;
                }
                return isLeaf;
            }

            // 递归构建子目录
            private ObservableList<TreeItem<File>> buildChildren(){
                if(file == null){
                    return FXCollections.emptyObservableList();
                }
                if(file.isFile()){
                    return FXCollections.emptyObservableList();
                }
                File[] files = file.listFiles();
                if(files != null){
                    ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();
                    for(File f : files){
                        children.add(createNode(f));
                    }
                    return children;
                }
                return FXCollections.emptyObservableList();
            }
            // 获取相对于根目录的路径
/*            private String getRelativePath(File f) {
                String rootPath = new File("").getAbsolutePath(); // 根目录的绝对路径
                String absolutePath = f.getAbsolutePath();
                String relativePath = new File(rootPath).toURI().relativize(new File(absolutePath).toURI()).getPath();
                return relativePath;
            }*/
        };
    }

}
