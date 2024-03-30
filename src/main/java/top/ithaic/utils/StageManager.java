package top.ithaic.utils;

import javafx.stage.Stage;

import java.util.Stack;


//TODO 使用方法：启动一个窗口时将其压入栈中，监听关闭窗口时间并将其弹出栈
public class StageManager {
    private static final Stack<Stage> stageStack = new Stack<>();
    public StageManager(){}
    public static void pushStage(Stage stack){
        stageStack.push(stack);
        for(int i =0 ;i<stageStack.size()-1;i++){
            Stage tmp = stageStack.elementAt(i);
            tmp.hide();
        }
    }

    public static void popStage(){
        stageStack.pop();
        Stage tmp = stageStack.peek();
        tmp.show();
    }
    public static Stack<Stage> getStageStack(){
        return stageStack;
    }
}
