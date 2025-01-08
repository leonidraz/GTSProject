package com.project_gts.project_gts.controllers.forms;

import com.project_gts.project_gts.MainApplication;
import javafx.fxml.FXML;

import java.io.IOException;

public class RegisterController {

    @FXML
    protected void switchToEntry() throws IOException{
        MainApplication.switchToScene("templates/EntryPage.fxml", MainApplication.getPrimaryStage());
    }
}