module elder.care.system.information.management.finals {
    requires java.sql;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires java.desktop;

    exports main to javafx.graphics;
    opens model to javafx.base;


}