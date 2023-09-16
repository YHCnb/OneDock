module com.yingtai.dock {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    requires AppTools;

    requires java.datatransfer;
    requires java.desktop;
    requires image4j;

    opens com.yingtai.dock to javafx.fxml;
    exports com.yingtai.dock;
}