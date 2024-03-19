module top.ithaic.showpicture {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens top.ithaic to javafx.fxml;
    exports top.ithaic;
    exports top.ithaic.imageview;
    opens top.ithaic.imageview to javafx.fxml;
    exports top.ithaic.listener;
    opens top.ithaic.listener to javafx.fxml;
    exports top.ithaic.shower;
    opens top.ithaic.shower to javafx.fxml;
    exports top.ithaic.utils;
    opens top.ithaic.utils to javafx.fxml;
    exports top.ithaic.Myinterface;
    opens top.ithaic.Myinterface to javafx.fxml;
    exports top.ithaic.shower.SlideShower;
    opens top.ithaic.shower.SlideShower to javafx.fxml;
    exports top.ithaic.shower.slidePlay;
    opens top.ithaic.shower.slidePlay to javafx.fxml;
}