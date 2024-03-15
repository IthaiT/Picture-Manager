package top.ithaic.utils;

import top.ithaic.shower.PictureShower;

import java.io.File;
import java.util.ArrayList;

import static java.lang.Math.max;

public final class ImageSearchUtil {
    public ImageSearchUtil(){}

    //供外部调用
    public void search(File searchPath,String searchName) throws InterruptedException {
        if(searchPath==null)return;

        SearchThread searchThread = new SearchThread(searchPath,searchName);
        WaitThread waitThread = new WaitThread(searchThread);
        waitThread.start();

    }

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
        private File[] searchResult;
        private SearchThread searchThread;
        public WaitThread(SearchThread searchThread){
            this.searchThread = searchThread;
        }
        @Override
        public void run() {
            this.searchThread.start();
            //阻塞waitThread
            try {
                this.searchThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //返回结果
            this.searchResult = new File[searchThread.getTempResult().size()];
            for(int i=0;i<searchThread.getTempResult().size();i++){
                searchResult[i] = searchThread.getTempResult().get(i);
            }
            //显示图片
            PathUtil.updateFiles(searchResult);
            new PictureShower().showPicture(searchResult);
        }
    }


    private class SearchThread extends Thread{
        private ArrayList<File> tempResult;
        private final String searchName;
        private final File searchRoot;
        public SearchThread(File searchRoot,String searchName){
            this.searchRoot = searchRoot;
            this.searchName = searchName;
            this.tempResult = new ArrayList<>();
        }
        @Override
        public void run(){
            searchImage(searchRoot,searchName);
        }

        public ArrayList<File> getTempResult(){
            return this.tempResult;
        }
        //递归搜索文件
        private void searchImage(File currentFile,String searchName){
            if(currentFile.isFile()){
                if(PictureUtil.isPicture(currentFile) && match(currentFile.getName(),searchName)){
                    System.out.println("匹配成功:"+currentFile.getName());
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
