module com.project_gts.project_gts {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.project_gts.project_gts to javafx.fxml;
    exports com.project_gts.project_gts;
    exports com.project_gts.project_gts.controllers.data;
    opens com.project_gts.project_gts.controllers.data to javafx.fxml;
    exports com.project_gts.project_gts.controllers.forms;
    opens com.project_gts.project_gts.controllers.forms to javafx.fxml;
}