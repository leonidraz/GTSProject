package com.project_gts.project_gts.controllers.forms;

import com.project_gts.project_gts.MainApplication;
import com.project_gts.project_gts.controllers.data.DatabaseConnection;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.HashMap;

public class MainController {
    @FXML
    private AnchorPane mainPanel;

    @FXML
    private VBox popUpMenu;

    @FXML
    private ComboBox<String> comboBoxGts;

    private boolean isMenuVisible = true;

    private double initialLayoutX;

    @FXML
    private Label namePage;

    @FXML
    private ImageView atsImageItemMenu;

    @FXML
    private Hyperlink atsLinkItemMenu;

    @FXML
    private Hyperlink mainLinkItemMenu;

    @FXML
    private ImageView mainImageItemMenu;

    @FXML
    private ImageView SubImageItemMenu;

    @FXML
    private Hyperlink SubLinkItemMenu;

    @FXML
    private ImageView PhoneImageItemMenu;

    @FXML
    private Hyperlink PhoneLinkItemMenu;

    @FXML
    private Hyperlink ReceiptLinkItemMenu;

    @FXML
    private ImageView ReceiptImageItemMenu;

    @FXML
    private ImageView RequestImageItemMenu;

    @FXML
    private Hyperlink RequestLinkItemMenu;

    @FXML
    private ImageView CallsImageItemMenu;

    @FXML
    private Hyperlink CallsLinkItemMenu;

    private HashMap<String, MenuItem> menuItems = new HashMap<>();

    private static final String ACTIVE_COLOR = "#D8C8F1";
    private static final String INACTIVE_COLOR = "#A195B3";

    private void initializeMenuItems() {
        menuItems.put("home", new MenuItem(mainImageItemMenu, mainLinkItemMenu, "homeIcon.png", INACTIVE_COLOR));
        menuItems.put("atc", new MenuItem(atsImageItemMenu, atsLinkItemMenu, "atcIcon.png", INACTIVE_COLOR));
        menuItems.put("subscriber", new MenuItem(SubImageItemMenu, SubLinkItemMenu, "subscriberIcon.png", INACTIVE_COLOR));
        menuItems.put("phone", new MenuItem(PhoneImageItemMenu, PhoneLinkItemMenu, "phoneIcon.png", INACTIVE_COLOR));
        menuItems.put("receipt", new MenuItem(ReceiptImageItemMenu, ReceiptLinkItemMenu, "receiptIcon.png", INACTIVE_COLOR));
        menuItems.put("request", new MenuItem(RequestImageItemMenu, RequestLinkItemMenu, "requestIcon.png", INACTIVE_COLOR));
        menuItems.put("call", new MenuItem(CallsImageItemMenu, CallsLinkItemMenu, "callIcon.png", INACTIVE_COLOR));
    }

    private void updateMenu(String activePage) {
        // Для каждого элемента меню устанавливаем неактивные свойства
        for (MenuItem item : menuItems.values()) {
            item.setColor(INACTIVE_COLOR);
            item.setImagePath(item.getImagePath().replace("Active", ""));
        }

        // Обновляем активный элемент
        if (menuItems.containsKey(activePage)) {
            MenuItem activeItem = menuItems.get(activePage);
            activeItem.setColor(ACTIVE_COLOR);
            activeItem.setImagePath(activePage + "IconActive.png");
        }
    }

    @FXML
    protected void initialize() {
        initialLayoutX = mainPanel.getLayoutX();

        initializeMenuItems();

        try (Connection connection = DatabaseConnection.getConnectionDB()) {
            ObservableList<String> gtsList = getGtsFromDatabase(connection);
            comboBoxGts.setItems(gtsList);

            loadPage("panel/GtsInfo.fxml");
            DatabaseConnection.closeConnectionDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void switchToEntry() throws IOException {
        MainApplication.switchToScene("templates/EntryPage.fxml", MainApplication.getPrimaryStage());
    }

    @FXML
    protected void toggleMenu() {
        if (isMenuVisible) {
            hideMenuWithFade();
        } else {
            showMenuWithFade();
        }
        isMenuVisible = !isMenuVisible;
    }

    void hideMenuWithFade() {
        popUpMenu.setVisible(false);
        popUpMenu.setManaged(false);
        mainPanel.setLayoutX((mainPanel.getParent().getBoundsInLocal().getWidth() - mainPanel.getPrefWidth()) / 2);

        // Анимация исчезновения
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), popUpMenu);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        //Анимация сдвига
        TranslateTransition transition = new TranslateTransition(Duration.millis(800), popUpMenu);
        transition.setFromX(popUpMenu.getLayoutX());
        transition.setToX(-200);

        fadeOut.play();
        transition.play();
    }

    void showMenuWithFade() {
        //Делаем меню видимым и возвращаем текущее положение
        popUpMenu.setVisible(true);
        popUpMenu.setManaged(true);
        mainPanel.setLayoutX(initialLayoutX);

        //Анимация появления
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), popUpMenu);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        //Анимация сдвига
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), popUpMenu);
        transition.setFromX(-200);
        transition.setToX(0);

        fadeIn.play();
        transition.play();
    }

    private ObservableList<String> getGtsFromDatabase(Connection connection) throws SQLException {
        ObservableList<String> gtsList = FXCollections.observableArrayList();
        String query = "SELECT name FROM gts";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                gtsList.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gtsList;
    }

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project_gts/project_gts/templates/" + fxmlPath));
            AnchorPane newPage = loader.load();
            mainPanel.getChildren().clear();
            mainPanel.getChildren().add(newPage);

            Object controller = loader.getController();

            try {
                Method setComboBoxMethod = controller.getClass().getMethod("setComboBox", ComboBox.class);
                try {
                    setComboBoxMethod.invoke(controller, comboBoxGts);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchMethodException e) {
                System.out.println("Метод setComboBox не найден в контроллере: " + controller.getClass().getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void switchToGtsPage() {
        loadPage("panel/GtsInfo.fxml");
        updateMenu("home");
        namePage.setText("Главная");
    }

    @FXML
    protected void switchToAtsPage() {
        loadPage("panel/AtsInfo.fxml");
        updateMenu("atc");
        namePage.setText("АТС");
    }

    @FXML
    protected void switchToSubscribersPage() {
        loadPage("panel/SubscribersInfo.fxml");
        updateMenu("subscriber");
        namePage.setText("Абоненты");
    }


    @FXML
    protected void switchToPhonesPage() {
        loadPage("panel/PhonesInfo.fxml");
        updateMenu("phone");
        namePage.setText("Телефоны");
    }

    @FXML
    protected void switchToReceiptsPage() {
        loadPage("panel/ReceiptsInfo.fxml");
        updateMenu("receipt");
        namePage.setText("Квитанции");
    }

    @FXML
    protected void switchToRequestPage() {
        loadPage("panel/RequestInfo.fxml");
        updateMenu("request");
        namePage.setText("Заявки");
    }

    @FXML
    protected void switchToCallsPage() {
        loadPage("panel/CallsInfo.fxml");
        updateMenu("call");
        namePage.setText("Звонки");
    }
}
