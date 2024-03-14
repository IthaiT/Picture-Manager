package top.ithaic.utils;

import java.io.File;
import java.util.ArrayList;

import static java.lang.Math.max;

public class ImageSearchUtil {
    private ArrayList<File> tempResult;
    private File[] searchResult;

    public ImageSearchUtil(){tempResult = new ArrayList<>();}

    //供外部调用
    public File[] search(File searchPath,String searchName) throws InterruptedException {
        if(searchPath==null)return null;
        SearchThread searchThread = new SearchThread(searchPath,searchName);
        searchThread.start();
        //TODO
        //   这里会阻塞主线程 需要修改
        searchThread.join();
        //处理结果 转成File数组
        this.searchResult = new File[tempResult.size()];
        for(int i=0;i<tempResult.size();i++){
            searchResult[i] = tempResult.get(i);
        }
        return searchResult;
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

    private class SearchThread extends Thread{
        private  boolean isTerminal;
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

        private void terminate(){this.isTerminal = true;}

        //递归搜索文件
        private void searchImage(File currentFile,String searchName){
            if(currentFile.isFile()&&!isTerminal){
                if(isPicture(currentFile) && match(currentFile.getName(),searchName)){
                    System.out.println("匹配成功:"+currentFile.getName());
                    tempResult.add(currentFile);
                }
                return;
            }
            if(currentFile.isDirectory()&&!isTerminal){
                File[] files = currentFile.listFiles();
                if(files!=null){
                    for(File file : files){
                        searchImage(file,searchName);
                    }
                }
            }
        }

        //判断是否为照片
        public boolean isPicture(File file){
            String[] formats = {".jpg",".jpeg",".bmp",".gif",".png"};
            boolean judge = false;
            for(String format :formats){
                if(file.getName().toLowerCase().endsWith(format))
                    judge = true;
            }
            return judge;
        }

    }
}
