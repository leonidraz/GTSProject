package com.project_gts.project_gts.controllers.forms;

import com.project_gts.project_gts.controllers.data.DatabaseConnection;
import com.project_gts.project_gts.controllers.data.TableViewUtil;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CallsInfoController {

    @FXML
    private ComboBox<String> comboBoxGts;

    @FXML
    private Label totalEntries;

    @FXML
    private TableView<ObservableList<Object>> CallsTable;

    @FXML
    private TextField filterByDistrict;

    @FXML
    private TextField filterByNumer;

    @FXML
    private ComboBox<String> filterByAts;

    @FXML
    private Label NumerLabel;

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
                updateCallsInfo(selectedGtsName, filterByNumer.getText(), filterByAts.getValue(), filterByDistrict.getText());
            }
        });

        if (comboBox.getValue() != null) {
            loadAtsComboBox(comboBox.getValue());
            updateCallsInfo(comboBoxGts.getValue(), filterByNumer.getText(), filterByAts.getValue(), filterByDistrict.getText());
        }
    }

    @FXML
    private void initialize() {
        addFilterListener(filterByNumer, NumerLabel, "Номер звонящего");
        addFilterListener(filterByDistrict, districtLabel, "Район");

        filterByAts.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateCallsInfo(comboBoxGts.getValue(), getFilterValue(filterByNumer), newValue, getFilterValue(filterByDistrict));
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
                updateCallsInfo(selectedGtsName, getFilterValue(filterByNumer), filterByAts.getValue(), getFilterValue(filterByDistrict));
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

    private void updateCallsInfo(String gtsName, String NumerFilter, String atsFilter, String districtFilter) {
        String query = buildQuery(
                "SELECT  " +
                        "    c.id, " +
                        "    p.number, " +
                        "    c.dialed_number, " +
                        "    c.date, " +
                        "    c.time, " +
                        "    c.duration " +
                        "FROM  " +
                        "    calls c " +
                        "JOIN  " +
                        "    phones p ON c.phone_id = p.id " +
                        "JOIN  " +
                        "    subscribers s ON p.subscriber_id = s.id " +
                        "JOIN  " +
                        "    address addr ON p.address_id = addr.id " +
                        "JOIN  " +
                        "    ats ats ON p.ats_id = ats.id " +
                        "JOIN  " +
                        "    gts g ON ats.gts_id = g.id " +
                        "WHERE g.name = ? ",
                NumerFilter, atsFilter, districtFilter
        );

        query += "GROUP BY c.id, p.number;";

        executeQuery(query, CallsTable, gtsName, NumerFilter, atsFilter, districtFilter);
        totalEntries.setText("Всего записей: " + CallsTable.getItems().size());
    }

    private String buildQuery(String baseQuery, String NumerFilter, String atsFilter, String districtFilter) {
        if (NumerFilter != null && !NumerFilter.isEmpty()) {
            baseQuery += " AND p.number LIKE ?";
        }

        if (atsFilter != null && !atsFilter.isEmpty()) {
            baseQuery += " AND ats.name = ?";
        }

        if (districtFilter != null && !districtFilter.isEmpty()) {
            baseQuery += " AND addr.district LIKE ?";
        }
        return baseQuery;
    }


    private void executeQuery(String query, TableView<ObservableList<Object>> tableView, String gtsName, String NumerFilter, String atsFilter, String districtFilter) {
        try (Connection connection = DatabaseConnection.getConnectionDB();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, gtsName);
            int paramIndex = 2;

            if (NumerFilter != null && !NumerFilter.isEmpty()) {
                statement.setString(paramIndex++, "%" + NumerFilter + "%");
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
