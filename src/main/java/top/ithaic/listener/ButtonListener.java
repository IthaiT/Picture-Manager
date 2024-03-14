package top.ithaic.listener;

import javafx.scene.control.Button;
import top.ithaic.Myinterface.Listener;
import top.ithaic.shower.PictureShower;
import top.ithaic.utils.PathUtil;

public class ButtonListener implements Listener {
    private static Button backward;
    private static Button forward;

    public ButtonListener(Button...buttons){
        ButtonListener.backward = buttons[0];
        ButtonListener.forward = buttons[1];
        Listen();
    }

    @Override
    public void Listen(){
        PictureShower pictureShower = new PictureShower();

        ButtonListener.backward.setOnMouseClicked(mouseEvent -> {
            if(PathUtil.getCurrentPath() !=null)
                pictureShower.showPicture(PathUtil.getCurrentPath().getParentFile());
        });

        ButtonListener.forward.setOnMouseClicked(mouseEvent -> {
            if(PathUtil.getLastPath() !=null&&PathUtil.getLastPath().getParentFile()!=null&&PathUtil.getLastPath().getParentFile().compareTo(PathUtil.getCurrentPath())==0)
                pictureShower.showPicture(PathUtil.getLastPath());
        });
    }
}
