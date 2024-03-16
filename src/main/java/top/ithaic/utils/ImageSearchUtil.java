package top.ithaic.utils;

import top.ithaic.shower.PictureShower;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;

import static java.lang.Math.max;

public final class ImageSearchUtil {
    private HashSet<File> isfindFiles;
    private CountDownLatch countDownLatch;
    private ArrayList<File> tempResult;
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

    //需要修改为KMP算法
    //匹配文件名
    private boolean match(String fileName,String searchName){
        if(fileName.length() < searchName.length())return false;
        //动态规划
        int[][] dp = new int[fileName.length()+1][searchName.length()+1];
        for(int i=1;i<=fileName.length();i++){
            for(int j=1;j<=searchName.length();j++){
                if(fileName.charAt(i-1) == searchName.charAt(j-1)){
                    dp[i][j] = dp[i-1][j-1] + 1;
                }
                else{
                    dp[i][j] = max(dp[i-1][j],dp[i][j-1]);
                }
            }
        }
        if(dp[fileName.length()][searchName.length()] == searchName.length()) return true;
        return false;
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
                    System.out.println("匹配成功:"+currentFile.getName());
                    if(!tempResult.contains(currentFile))tempResult.add(currentFile);
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
            isfindFiles.add(currentFile);
        }



    }
}
