package top.ithaic.utils;

import top.ithaic.shower.PictureShower;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.regex.*;

public final class ImageSearchUtil {
    private final HashSet<File> isfindFiles;
    private CountDownLatch countDownLatch;
    private final ArrayList<File> tempResult;
    public ImageSearchUtil(){
        this.tempResult = new ArrayList<>();
        this.isfindFiles = new HashSet<>();
    }

    //供外部调用,支持多线程搜索
    public void search(File searchPath,String searchName,int threadNumber) throws InterruptedException {
        if(searchPath==null)return;
        countDownLatch = new CountDownLatch(threadNumber);
        WaitThread waitThread = new WaitThread(searchPath,searchName,threadNumber);
        waitThread.start();
    }

    //默认单线程搜索
    public void search(File searchPath,String searchName) throws InterruptedException {this.search(searchPath,searchName,1);}

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
        private int threadNumber;
        private File[] searchResult;
        public WaitThread(File searchPath,String searchName,int threadNumber){
            this.searchPath = searchPath;
            this.searchName = searchName;
            this.threadNumber = threadNumber;
        }
        @Override
        public void run() {
            for(int i=1;i<=threadNumber;i++){
                new SearchThread(searchPath,searchName).start();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //返回结果
            this.searchResult = new File[tempResult.size()];
            for(int i=0;i<tempResult.size();i++){
                searchResult[i] = tempResult.get(i);
            }
            //显示图片
            PathUtil.updateFiles(searchResult);
            new PictureShower().showPicture(searchResult);
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
            countDownLatch.countDown();
        }

        //递归搜索文件
        private void searchImage(File currentFile,String searchName){
            if(currentFile.isFile()&&!isfindFiles.contains(currentFile)){
                if(PictureUtil.isPicture(currentFile) && match(currentFile.getName().toLowerCase(),searchName)){
                    synchronized (tempResult) {
                        if (!tempResult.contains(currentFile)) tempResult.add(currentFile);
                    }
                }
                return;
            }
            if(currentFile.isDirectory()&&!isfindFiles.contains(currentFile)){
                File[] files = currentFile.listFiles();
                if(files!=null){
                    for(File file : files){
                        searchImage(file,searchName);
                    }
                }
            }
            synchronized (isfindFiles) {
                isfindFiles.add(currentFile);
            }
        }


    }
}
