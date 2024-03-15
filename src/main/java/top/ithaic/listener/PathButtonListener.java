package top.ithaic.listener;

import javafx.scene.control.Button;
import top.ithaic.Myinterface.Listener;
import top.ithaic.shower.PictureShower;
import top.ithaic.utils.PathUtil;

public class PathButtonListener implements Listener {
    private static Button backward;
    private static Button forward;

    public PathButtonListener(Button...buttons){
        PathButtonListener.backward = buttons[0];
        PathButtonListener.forward = buttons[1];
        Listen();
    }

    @Override
    public void Listen(){
        PictureShower pictureShower = new PictureShower();

        PathButtonListener.backward.setOnMouseClicked(mouseEvent -> {
            if(PathUtil.getCurrentPath() !=null)
                pictureShower.showPicture(PathUtil.getCurrentPath().getParentFile());
        });

        PathButtonListener.forward.setOnMouseClicked(mouseEvent -> {
            if(PathUtil.getLastPath() !=null&&PathUtil.getLastPath().getParentFile()!=null&&PathUtil.getLastPath().getParentFile().compareTo(PathUtil.getCurrentPath())==0)
                pictureShower.showPicture(PathUtil.getLastPath());
        });
    }
}
