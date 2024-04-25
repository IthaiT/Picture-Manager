package top.ithaic.utils;

import top.ithaic.shower.PictureShower;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.*;

public final class ImageSearchUtil {
    private static boolean isSearching = false;
    private final ArrayList<File> tempResult;

    public ImageSearchUtil(){
        this.tempResult = new ArrayList<>();
    }

    //供外部调用
    public void search(File searchPath,String searchName) throws InterruptedException {
        if(searchPath==null)return;
        WaitThread waitThread = new WaitThread(searchPath,searchName);
        waitThread.start();
    }

    //匹配文件名
    private boolean match(String fileName,String searchName){
        // 编译正则表达式
        Pattern pattern = Pattern.compile(searchName);
        // 创建匹配器
        Matcher matcher = pattern.matcher(fileName);
        // 尝试查找匹配项
        return matcher.find();
    }

    //等待线程
    private class WaitThread extends Thread{
        private File searchPath;
        private String searchName;
        private File[] searchResult;
        public WaitThread(File searchPath,String searchName){
            this.searchPath = searchPath;
            this.searchName = searchName;
        }
        @Override
        public void run() {
            //若是存在搜索线程正在运行，则返回
            if(isSearching)return;

            isSearching = true;
            SearchThread searchThread = new SearchThread(searchPath,searchName);
            searchThread.start();
            //阻塞等待线程,等待返回结果
            try {
                searchThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //返回结果
            this.searchResult = new File[tempResult.size()];
            for(int i=0;i<tempResult.size();i++) searchResult[i] = tempResult.get(i);
            //显示图片
            FilePathUtil.updateFiles(searchResult);
            new PictureShower().showPicture(searchResult);
            isSearching = false;
        }
    }


    private class SearchThread extends Thread{
        private final String searchName;
        private final File searchRoot;
        public SearchThread(File searchRoot,String searchName){
            this.searchRoot = searchRoot;
            this.searchName = searchName;
        }
        @Override
        public void run(){
            searchImage(searchRoot,searchName);
        }

        //递归搜索文件
        private void searchImage(File currentFile,String searchName){
            if(currentFile.isFile()){
                if(PictureUtil.isPicture(currentFile) && match(currentFile.getName().toLowerCase(),searchName)){
                    tempResult.add(currentFile);
                }
                return;
            }
            if(currentFile.isDirectory()){
                File[] files = currentFile.listFiles();
                if(files!=null){
                    for(File file : files){
                        searchImage(file,searchName);
                    }
                }
            }
        }



    }
}
