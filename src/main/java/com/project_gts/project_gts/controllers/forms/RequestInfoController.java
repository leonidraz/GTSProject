package com.project_gts.project_gts.controllers.forms;

import com.project_gts.project_gts.controllers.data.DatabaseConnection;
import com.project_gts.project_gts.controllers.data.TableViewUtil;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RequestInfoController {

    @FXML
    private ComboBox<String> comboBoxGts;

    @FXML
    private Label totalEntries;

    @FXML
    private TableView<ObservableList<Object>> RequestTable;

    @FXML
    private TextField filterByDistrict;

    @FXML
    private TextField filterByFio;

    @FXML
    private ComboBox<String> filterByAts;

    @FXML
    private Label fioLabel;

    @FXML
    private Label districtLabel;

    @FXML
    private Label atsNameLabel;

    public void setComboBox(ComboBox<String> comboBox) {
        this.comboBoxGts = comboBox;

        this.comboBoxGts.setOnAction(event -> {
            String selectedGtsName = comboBoxGts.getValue();
            if (selectedGtsName != null && !selectedGtsName.isEmpty()) {
                loadAtsComboBox(selectedGtsName);
                updateRequestInfo(selectedGtsName, filterByFio.getText(), filterByAts.getValue(), filterByDistrict.getText());
            }
        });

        if (comboBox.getValue() != null) {
            loadAtsComboBox(comboBox.getValue());
            updateRequestInfo(comboBoxGts.getValue(), filterByFio.getText(), filterByAts.getValue(), filterByDistrict.getText());
        }
    }

    @FXML
    private void initialize() {
        addFilterListener(filterByFio, fioLabel, "ФИО");
        addFilterListener(filterByDistrict, districtLabel, "Район");

        filterByAts.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateRequestInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), newValue, getFilterValue(filterByDistrict));
            if (filterByAts.getValue() != null && !filterByAts.getValue().isEmpty()) {
                atsNameLabel.setVisible(false);
            } else {
                atsNameLabel.setVisible(true);
            }
        });
    }

    private void addFilterListener(TextField filterField, Label label, String labelText) {
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedGtsName = comboBoxGts.getValue();
            if (selectedGtsName != null && !selectedGtsName.isEmpty()) {
                updateRequestInfo(selectedGtsName, getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
            }
        });

        filterField.focusedProperty().addListener((observable, oldValue, newValue) -> handleFocusChange(newValue, label, labelText));
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

    private String getFilterValue(TextField filterField) {
        return filterField.getText();
    }

    private void playFadeTransition(Label label, double from, double to, Runnable onFinished) {
        FadeTransition fade = new FadeTransition(Duration.seconds(0.3), label);
        fade.setFromValue(from);
        fade.setToValue(to);
        fade.setOnFinished(event -> onFinished.run());
        fade.play();
    }

    private void loadAtsComboBox(String gtsName) {
        String query = "SELECT ats.name FROM ats JOIN gts ON ats.gts_id = gts.id WHERE gts.name = ?";

        try (Connection connection = DatabaseConnection.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, gtsName);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<String> atsList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                atsList.add(resultSet.getString("name"));
            }

            filterByAts.setItems(atsList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnectionDB();
        }
    }


    private void updateRequestInfo(String gtsName, String fioFilter, String atsFilter, String districtFilter) {
        String query = buildQuery(
                "SELECT " +
                        "    DISTINCT r.id, " +
                        "    s.last_name, " +
                        "    s.first_name, " +
                        "    s.middle_name, " +
                        "    r.request_date, " +
                        "    r.type AS request_type, " +
                        "    r.description, " +
                        "    r.prioritet, " +
                        "    r.status " +
                        "FROM " +
                        "    requests r " +
                        "JOIN subscribers s ON r.subscriber_id = s.id " +
                        "JOIN phones p ON s.id = p.subscriber_id  " +
                        "JOIN ats ON p.ats_id = ats.id  " +
                        "JOIN gts ON ats.gts_id = gts.id  " +
                        "JOIN address addr ON p.address_id = addr.id  " +
                        "WHERE gts.name = ? ",
                fioFilter, atsFilter, districtFilter
        );

        query += " ORDER BY r.request_date DESC, r.id;";

        executeQuery(query, RequestTable, gtsName, fioFilter, atsFilter, districtFilter);
        totalEntries.setText("Всего записей: " + RequestTable.getItems().size());
    }

    private String buildQuery(String baseQuery, String fioFilter, String atsFilter, String districtFilter) {
        if (fioFilter != null && !fioFilter.isEmpty()) {
            baseQuery += " AND CONCAT(s.last_name, ' ', s.first_name, ' ', s.middle_name) LIKE ?";
        }

        if (atsFilter != null && !atsFilter.isEmpty()) {
            baseQuery += " AND ats.name = ?";
        }

        if (districtFilter != null && !districtFilter.isEmpty()) {
            baseQuery += " AND addr.district LIKE ?";
        }
        return baseQuery;
    }


    private void executeQuery(String query, TableView<ObservableList<Object>> tableView, String gtsName, String fioFilter, String atsFilter, String districtFilter) {
        try (Connection connection = DatabaseConnection.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, gtsName);
            int paramIndex = 2;

            if (fioFilter != null && !fioFilter.isEmpty()) {
                statement.setString(paramIndex++, "%" + fioFilter + "%");
            }
            if (atsFilter != null && !atsFilter.isEmpty()) {
                statement.setString(paramIndex++, atsFilter);
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
}
