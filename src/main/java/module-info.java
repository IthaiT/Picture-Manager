module top.ithaic.showpicture {
    requires javafx.controls;
    requires javafx.fxml;


    opens top.ithaic.showpicture to javafx.fxml;
    exports top.ithaic.showpicture;
}