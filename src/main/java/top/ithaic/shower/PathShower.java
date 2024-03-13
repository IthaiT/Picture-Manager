package top.ithaic.shower;

import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class PathShower {
    private TextField pathShower;
    private AnchorPane anchorPane;
    public PathShower(TextField pathShower,AnchorPane father){
        this.pathShower = pathShower;
        this.anchorPane = father;
        this.pathShower.setEditable(false);
        this.addListener();
    }

    public void addListener(){
        this.pathShower.textProperty().bind(new StringBinding() {
            {
                bind(PictureShower.getCurrentPathProperty());
            }
            @Override
            protected String computeValue() {
                return PictureShower.getCurrentPathProperty().getValue();
            }
        });

        ChangeListener<Number> anchorPaneSizeListener = ((observableValue, oldSize, newSize) -> {
            double newWidth = anchorPane.getWidth();
            this.pathShower.setPrefWidth(newWidth - 400);
        });
        this.anchorPane.widthProperty().addListener(anchorPaneSizeListener);
    }
}
