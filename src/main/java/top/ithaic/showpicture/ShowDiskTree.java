package top.ithaic.showpicture;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.ArrayList;

public class ShowDiskTree {
    public class MyFile {
        private File file;
        private String filename;
        public MyFile(File file){
            this.file = file;
            this.filename = file.getName();
        }
        public MyFile(File file,String filename){
            this.file = file;
            this.filename = filename;
        }
        @Override
        public String toString(){
            return this.filename;
        }
    }

    public ShowDiskTree(TreeView disktree){
        //设置树视图的根目录
        disktree.setRoot(new TreeItem(new MyFile(new File("此电脑"))));
        //获取电脑磁盘分区
        ArrayList<MyFile> diskPartitions = getDiskPartitions();
        for(MyFile partition : diskPartitions){
            //递归获取每一个分区的目录
            TreeItem<MyFile> item = createNode(partition);
            //将分区添加到“此电脑”根节点
            disktree.getTreeItem(0).getChildren().add(item);
            System.out.println(partition.toString());
        }
    }

    // TODO 获取电脑磁盘分区
    private ArrayList<MyFile> getDiskPartitions(){
        ArrayList<MyFile> diskPartitions = new ArrayList<>();
        File[] disks = File.listRoots();
        if(disks == null){
            return null;
        }
        for(File partition : disks){
            diskPartitions.add(new MyFile(partition,partition.getPath()));
        }
        return diskPartitions;
    }

    private TreeItem<MyFile> createNode(MyFile file){
        return new TreeItem<>(file){
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
                    isLeaf = f.file.isFile() ;
                }
                return isLeaf;
            }

            // 递归构建子目录
            private ObservableList<TreeItem<MyFile>> buildChildren(){
                if(file.file == null){
                    return FXCollections.emptyObservableList();
                }
                if(file.file.isFile()){
                    return FXCollections.emptyObservableList();
                }
                File[] files = file.file.listFiles();
                if(files != null){
                    ObservableList<TreeItem<MyFile>> children = FXCollections.observableArrayList();
                    for(File f : files){
                        children.add(createNode(new MyFile(f)));
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
