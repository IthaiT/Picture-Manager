package top.ithaic.listener;

import javafx.scene.control.Button;
import top.ithaic.shower.PictureShower;

public class ButtonListener {
    private static Button backward;
    private static Button forward;

    public ButtonListener(Button...buttons){
        ButtonListener.backward = buttons[0];
        ButtonListener.forward = buttons[1];
        addButtonListener();
    }

    public void addButtonListener(){
        PictureShower pictureShower = new PictureShower();
        ButtonListener.backward.setOnMouseClicked(mouseEvent -> {
            if(PictureShower.getCurrentPath()!=null)
                pictureShower.showPicture(PictureShower.getCurrentPath().getParentFile());
        });
        ButtonListener.forward.setOnMouseClicked(mouseEvent -> {
            if(PictureShower.getLastPath()!=null)
                pictureShower.showPicture(PictureShower.getLastPath());
        });
    }
}
