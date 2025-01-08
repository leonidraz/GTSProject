package com.project_gts.project_gts.controllers.forms;

import com.project_gts.project_gts.MainApplication;
import com.project_gts.project_gts.controllers.data.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import java.io.InputStream;

public class EntryController {

    @FXML
    private AnchorPane adminPanel;

    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    protected void switchToRegister() throws IOException{
        MainApplication.switchToScene("templates/RegisterPage.fxml", MainApplication.getPrimaryStage());
    }

    @FXML
    protected void switchToAdminPanel() throws IOException {
       adminPanel.setVisible(true);
    }

    @FXML
    protected void closeAdminPanel() throws IOException {
        adminPanel.setVisible(false);
    }

    @FXML
    protected void loginAsAdmin() {
        try {
            // Проверка подключения к базе данных
            Connection connection = DatabaseConnection.getConnectionDB();
            if (connection == null) {
                showAlert("Ошибка", "Не удалось подключиться к базе данных.");
                return;
            }

            // Логика проверки логина и пароля
            Properties properties = new Properties();
            try (InputStream input = getClass().getResourceAsStream("/config.properties")) {
                if (input == null) {
                    showAlert("Ошибка", "Файл config.properties не найден.");
                    return;
                }
                properties.load(input);
            }

            String adminUsername = properties.getProperty("admin.username");
            String adminPassword = properties.getProperty("admin.password");

            String enteredUsername = usernameField.getText();
            String enteredPassword = passwordField.getText();

            if (enteredUsername.equals(adminUsername) && enteredPassword.equals(adminPassword)) {
                MainApplication.switchToScene("templates/MainPage.fxml", MainApplication.getPrimaryStage());
            } else {
                showAlert("Ошибка", "Неверный логин или пароль.");
            }
            DatabaseConnection.closeConnectionDB();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Произошла ошибка: " + e.getMessage());
        }
    }


    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);  // Заголовок окна
        alert.setHeaderText(null);  // Можно оставить пустым
        alert.setContentText(message);  // Сообщение в окне

        // Добавляем кнопку для закрытия
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }
}