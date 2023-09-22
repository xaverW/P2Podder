module p2podder {

    opens de.p2tools.p2podder;
    exports de.p2tools.p2podder;
    
    opens de.p2tools.p2podder.controller.data.podcast;
    opens de.p2tools.p2podder.controller.data;

    requires de.p2tools.p2lib;
    requires javafx.controls;
    requires org.controlsfx.controls;
    requires java.datatransfer;
    requires org.apache.commons.io;
    requires rome;
    requires org.apache.commons.lang3;
    requires java.desktop;
    requires commons.cli;
    requires jdom;
}

