package com.project_gts.project_gts.controllers.forms;

import com.project_gts.project_gts.controllers.data.DatabaseConnection;
import com.project_gts.project_gts.controllers.data.TableViewUtil;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AtsInfoController {

    @FXML
    private ComboBox<String> comboBoxGts;

    @FXML
    private TableView<ObservableList<Object>> atsTable;

    @FXML
    private Label totalEntries;

    @FXML
    private TextField filterByName;

    @FXML
    private Label atsName;

    @FXML
    private TextField filterByType;

    @FXML
    private TextField filterByDistrict;

    @FXML
    private Label atsType;

    @FXML
    private Label atsDistrict;

    public void setComboBox(ComboBox<String> comboBox) {
        this.comboBoxGts = comboBox;

        this.comboBoxGts.setOnAction(event -> {
            String selectedGtsName = comboBoxGts.getValue();
            if (selectedGtsName != null && !selectedGtsName.isEmpty()) {
                updateAtsInfo(selectedGtsName, filterByName.getText(), filterByType.getText(), filterByDistrict.getText());
            }
        });

        if (comboBox.getValue() != null) {
            updateAtsInfo(comboBox.getValue(), filterByName.getText(), filterByType.getText(), filterByDistrict.getText());
        }
    }

    @FXML
    private void initialize() {
        addFilterListener(filterByName, atsName, "Название");
        addFilterListener(filterByType, atsType, "Тип");
        addFilterListener(filterByDistrict, atsDistrict, "Район");
    }

    private void addFilterListener(TextField filterField, Label label, String labelText) {
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedGtsName = comboBoxGts.getValue();
            if (selectedGtsName != null && !selectedGtsName.isEmpty()) {
                updateAtsInfo(selectedGtsName, getFilterValue(filterByName), getFilterValue(filterByType), getFilterValue(filterByDistrict));
            }
        });

        filterField.focusedProperty().addListener((observable, oldValue, newValue) -> handleFocusChange(newValue, label, labelText));
    }

    private String getFilterValue(TextField filterField) {
        return filterField.getText();
    }

    private void handleFocusChange(Boolean focused, Label label, String labelText) {
        if (focused) {
            playFadeTransition(label, 1.0, 0.0, () -> {
                label.setText("");
                label.setPadding(new javafx.geometry.Insets(0));
            });
        } else {
            playFadeTransition(label, 0.0, 1.0, () -> {
                label.setText(labelText);
                label.setPadding(new javafx.geometry.Insets(0, 10, 0, 10));
            });
        }
    }

    private void playFadeTransition(Label label, double fromValue, double toValue, Runnable onFinished) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), label);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.setOnFinished(event -> onFinished.run());
        fadeTransition.play();
    }

    private void updateAtsInfo(String gtsName, String nameFilter, String typeFilter, String districtFilter) {
        String query = buildQuery(
                "SELECT a.* FROM ats a JOIN gts g ON a.gts_id = g.id WHERE g.name = ?",
                nameFilter, typeFilter, districtFilter
        );

        executeQuery(query, atsTable, gtsName, nameFilter, typeFilter, districtFilter);
        totalEntries.setText("Всего записей: " + atsTable.getItems().size());
    }

    private String buildQuery(String baseQuery, String nameFilter, String typeFilter, String districtFilter) {
        if (nameFilter != null && !nameFilter.isEmpty()) {
            baseQuery += " AND a.name LIKE ?";
        }
        if (typeFilter != null && !typeFilter.isEmpty()) {
            baseQuery += " AND a.type LIKE ?";
        }
        if (districtFilter != null && !districtFilter.isEmpty()) {
            baseQuery += " AND a.district LIKE ?";
        }
        return baseQuery;
    }

    private void executeQuery(String query, TableView<ObservableList<Object>> tableView, String gtsName, String nameFilter, String typeFilter, String districtFilter) {
        try (Connection connection = DatabaseConnection.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, gtsName);
            int paramIndex = 2;

            if (nameFilter != null && !nameFilter.isEmpty()) {
                statement.setString(paramIndex++, "%" + nameFilter + "%");
            }
            if (typeFilter != null && !typeFilter.isEmpty()) {
                statement.setString(paramIndex++, "%" + typeFilter + "%");
            }
            if (districtFilter != null && !districtFilter.isEmpty()) {
                statement.setString(paramIndex++, "%" + districtFilter + "%");
            }

            TableViewUtil.populateTable(tableView, statement, connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnectionDB();
        }
    }

    @FXML
    private void handleDebtorsButtonClick() {
        String query = buildQuery(
                "SELECT  " +
                        "    a.id,  " +
                        "    a.name,  " +
                        "    a.type,  " +
                        "    a.district,  " +
                        "    COUNT(DISTINCT ab.id) AS debtors_count, " +
                        "    SUM(r.total_to_pay - COALESCE(p.total_paid, 0)) AS total_debt  " +
                        "FROM ats a " +
                        "JOIN gts g ON a.gts_id = g.id " +
                        "LEFT JOIN ( " +
                        "    SELECT DISTINCT ph.subscriber_id, ph.ats_id " +
                        "    FROM phones ph " +
                        ") ph ON ph.ats_id = a.id  " +
                        "LEFT JOIN subscribers ab ON ab.id = ph.subscriber_id " +
                        "LEFT JOIN receipts r ON r.subscriber_id = ab.id " +
                        "LEFT JOIN ( " +
                        "    SELECT p.receipt_id, SUM(p.payment_amount) AS total_paid " +
                        "    FROM payments p " +
                        "    GROUP BY p.receipt_id " +
                        ") p ON r.id = p.receipt_id " +
                        "WHERE g.name = ? " +
                        "AND r.status IN ('Не оплачено', 'Частично оплачено') ",
                filterByName.getText(), filterByType.getText(), filterByDistrict.getText()
        );

        query += " GROUP BY a.id, a.name, a.type, a.district ORDER BY a.name;";

        executeQuery(query, atsTable, comboBoxGts.getValue(), filterByName.getText(), filterByType.getText(), filterByDistrict.getText());
    }
}