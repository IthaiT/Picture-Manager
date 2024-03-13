module top.ithaic.showpicture {
    requires javafx.controls;
    requires javafx.fxml;


    opens top.ithaic to javafx.fxml;
    exports top.ithaic;
    exports top.ithaic.imageview;
    opens top.ithaic.imageview to javafx.fxml;
    exports top.ithaic.listener;
    opens top.ithaic.listener to javafx.fxml;
    exports top.ithaic.shower;
    opens top.ithaic.shower to javafx.fxml;
}