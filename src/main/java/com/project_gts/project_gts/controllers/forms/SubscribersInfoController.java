package com.project_gts.project_gts.controllers.forms;

import com.project_gts.project_gts.controllers.data.DatabaseConnection;
import com.project_gts.project_gts.controllers.data.TableViewUtil;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SubscribersInfoController {

    @FXML
    private ComboBox<String> comboBoxGts;

    @FXML
    private Label totalEntries;

    @FXML
    private TableView<ObservableList<Object>> SubscribersTable;

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

    @FXML
    private AnchorPane SubscribersFilterPanel;

    @FXML
    private CheckBox cityCheckbox;

    @FXML
    private CheckBox departmentCheckbox;

    @FXML
    private CheckBox institutionCheckbox;

    @FXML
    private CheckBox privilegedCheckbox;

    @FXML
    private CheckBox regularCheckbox;

    @FXML
    private CheckBox mainPhoneCheckbox;

    @FXML
    private CheckBox parallelPhoneCheckbox;

    @FXML
    private CheckBox pairedPhoneCheckbox;

    @FXML
    private ComboBox<String> operatorComboBox;

    @FXML
    private TextField ageFilterField;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private RadioButton allRadioButton;

    @FXML
    private ToggleGroup genderToggleGroup;

    public void setComboBox(ComboBox<String> comboBox) {
        this.comboBoxGts = comboBox;

        this.comboBoxGts.setOnAction(event -> {
            String selectedGtsName = comboBoxGts.getValue();
            if (selectedGtsName != null && !selectedGtsName.isEmpty()) {
                loadAtsComboBox(selectedGtsName);
                updateSubscribersInfo(selectedGtsName, filterByFio.getText(), filterByAts.getValue(), filterByDistrict.getText());
            }
        });

        if (comboBox.getValue() != null) {
            loadAtsComboBox(comboBox.getValue());
            updateSubscribersInfo(comboBoxGts.getValue(), filterByFio.getText(), filterByAts.getValue(), filterByDistrict.getText());
        }
    }

    @FXML
    private void initialize() {
        addFilterListener(filterByFio, fioLabel, "ФИО");
        addFilterListener(filterByDistrict, districtLabel, "Район");

        operatorComboBox.setItems(FXCollections.observableArrayList(">", "<", ">=", "<=", "="));

        genderToggleGroup = new ToggleGroup();

        // Присваиваем ToggleGroup каждой из радиокнопок
        maleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setToggleGroup(genderToggleGroup);
        allRadioButton.setToggleGroup(genderToggleGroup);

        filterByAts.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSubscribersInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), newValue, getFilterValue(filterByDistrict));
            if (filterByAts.getValue() != null && !filterByAts.getValue().isEmpty()) {
                atsNameLabel.setVisible(false);
            } else {
                atsNameLabel.setVisible(true);
            }
        });

        cityCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateSubscribersInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
        });
        departmentCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateSubscribersInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
        });
        institutionCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateSubscribersInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
        });

        privilegedCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateSubscribersInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
        });
        regularCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateSubscribersInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
        });
        mainPhoneCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateSubscribersInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
        });
        parallelPhoneCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateSubscribersInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
        });

        pairedPhoneCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateSubscribersInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
        });

        ageFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSubscribersInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
        });

        operatorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSubscribersInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
        });

        genderToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateSubscribersInfo(comboBoxGts.getValue(), getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
            }
        });
    }

    private void addFilterListener(TextField filterField, Label label, String labelText) {
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedGtsName = comboBoxGts.getValue();
            if (selectedGtsName != null && !selectedGtsName.isEmpty()) {
                updateSubscribersInfo(selectedGtsName, getFilterValue(filterByFio), filterByAts.getValue(), getFilterValue(filterByDistrict));
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


    private void updateSubscribersInfo(String gtsName, String fioFilter, String atsFilter, String districtFilter) {
        String query = buildQuery(
                "SELECT  " +
                        "    DISTINCT s.id, " +
                        "    s.last_name, " +
                        "    s.first_name, " +
                        "    s.middle_name, " +
                        "    s.gender, " +
                        "    s.age, " +
                        "    s.type, " +
                        "    s.intercity, " +
                        "    addr.city, " +
                        "    addr.district, " +
                        "    addr.street, " +
                        "    addr.building, " +
                        "    addr.apartment " +
                        "FROM  " +
                        "    gts g " +
                        "JOIN " +
                        "    ats ats ON ats.gts_id = g.id " +
                        "JOIN  " +
                        "    phones p ON p.ats_id = ats.id " +
                        "JOIN  " +
                        "    subscribers s ON p.subscriber_id = s.id " +
                        "JOIN  " +
                        "    address addr ON p.address_id = addr.id " +
                        "WHERE  " +
                        "    g.name = ?",
                fioFilter, atsFilter, districtFilter
        );

        executeQuery(query, SubscribersTable, gtsName, fioFilter, atsFilter, districtFilter);
        totalEntries.setText("Всего записей: " + SubscribersTable.getItems().size());
    }

    private String getAtsTypeFilter() {
        StringBuilder atsFilter = new StringBuilder();
        if (cityCheckbox.isSelected()) {
            atsFilter.append("ats.type = 'Городская' OR ");
        }
        if (departmentCheckbox.isSelected()) {
            atsFilter.append("ats.type = 'Ведомственная' OR ");
        }
        if (institutionCheckbox.isSelected()) {
            atsFilter.append("ats.type = 'Учрежденческая' OR ");
        }

        if (atsFilter.length() > 0) {
            atsFilter.setLength(atsFilter.length() - 4);
        } else {
            atsFilter.append("1=1");
        }

        return atsFilter.toString();
    }

    private String getSubscriberCategoryFilter() {
        StringBuilder categoryFilter = new StringBuilder();
        if (privilegedCheckbox.isSelected()) {
            categoryFilter.append("s.type = 'льготник' OR ");
        }
        if (regularCheckbox.isSelected()) {
            categoryFilter.append("s.type = 'обычный' OR ");
        }

        if (categoryFilter.length() > 0) {
            categoryFilter.setLength(categoryFilter.length() - 4);
        } else {
            categoryFilter.append("1=1");
        }

        return categoryFilter.toString();
    }

    private String getPhoneTypeFilter() {
        StringBuilder phoneTypeFilter = new StringBuilder();
        if (mainPhoneCheckbox.isSelected()) {
            phoneTypeFilter.append("p.type = 'Основной' OR ");
        }
        if (parallelPhoneCheckbox.isSelected()) {
            phoneTypeFilter.append("p.type = 'Параллельный' OR ");
        }
        if (pairedPhoneCheckbox.isSelected()) {
            phoneTypeFilter.append("p.type = 'Спаренный' OR ");
        }

        if (phoneTypeFilter.length() > 0) {
            phoneTypeFilter.setLength(phoneTypeFilter.length() - 4);
        } else {
            phoneTypeFilter.append("1=1");
        }

        return phoneTypeFilter.toString();
    }

    private String getAgeFilter() {
        String operator = operatorComboBox.getValue();
        String ageValue = ageFilterField.getText();

        if (operator != null && ageValue != null && !ageValue.isEmpty()) {
            try {
                Integer.parseInt(ageValue);
                return "s.age " + operator + " ?";
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private String getGenderFilter() {
        if (maleRadioButton.isSelected()) {
            return "s.gender = 'М'";
        } else if (femaleRadioButton.isSelected()) {
            return "s.gender = 'Ж'";
        } else {
            return "";
        }
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

        String genderFilter = getGenderFilter();
        if (!genderFilter.isEmpty()) {
            baseQuery += " AND (" + genderFilter + ")";
        }

        String atsTypeFilter = getAtsTypeFilter();
        if (!atsTypeFilter.isEmpty()) {
            baseQuery += " AND (" + atsTypeFilter + ")";
        }

        String categoryFilter = getSubscriberCategoryFilter();
        if (!categoryFilter.isEmpty()) {
            baseQuery += " AND (" + categoryFilter + ")";
        }

        String phoneTypeFilter = getPhoneTypeFilter();
        if (!phoneTypeFilter.isEmpty()) {
            baseQuery += " AND (" + phoneTypeFilter + ")";
        }

        String ageFilter = getAgeFilter();
        if (!ageFilter.isEmpty()) {
            baseQuery += " AND (" + ageFilter + ")";
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

            String ageValue = ageFilterField.getText();
            if (ageValue != null && !ageValue.isEmpty()) {
                try {
                    statement.setInt(paramIndex++, Integer.parseInt(ageValue));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            TableViewUtil.populateTable(tableView, statement, connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnectionDB();
        }
    }

    @FXML
    private void handleRatioSubscribersButtonClick() {
        String query = buildQuery(
                "SELECT  " +
                        "    COUNT(DISTINCT CASE WHEN s.type = 'обычный' THEN s.id END) AS regular_count, " +
                        "COUNT(DISTINCT CASE WHEN s.type = 'льготник' THEN s.id END) AS privileged_count, " +
                        "ROUND(COUNT(DISTINCT CASE WHEN s.type = 'обычный' THEN s.id END) * 100.0 / COUNT(DISTINCT s.id), 2) AS regular_percentage, " +
                        "ROUND(COUNT(DISTINCT CASE WHEN s.type = 'льготник' THEN s.id END) * 100.0 / COUNT(DISTINCT s.id), 2) AS privileged_percentage " +
                        "FROM  " +
                        "    subscribers s " +
                        "JOIN  " +
                        "    phones p ON s.id = p.subscriber_id " +
                        "JOIN  " +
                        "    ats ON p.ats_id = ats.id " +
                        "JOIN gts ON ats.gts_id = gts.id " +
                        "JOIN address addr ON p.address_id = addr.id " +
                        "WHERE gts.name = ? ",
                filterByFio.getText(), filterByAts.getValue(), filterByDistrict.getText()
        );

        executeQuery(query, SubscribersTable, comboBoxGts.getValue(), filterByFio.getText(), filterByAts.getValue(), filterByDistrict.getText());
    }

    @FXML
    private void handleGroupByLastNameButtonClick() {
        String query = buildQuery(
                "SELECT  " +
                        "    s.last_name, " +
                        "    COUNT(DISTINCT s.id) AS total_subscribers " +
                        "FROM  " +
                        "    subscribers s " +
                        "JOIN  " +
                        "    phones p ON s.id = p.subscriber_id " +
                        "JOIN  " +
                        "    ats ON p.ats_id = ats.id " +
                        "JOIN gts ON ats.gts_id = gts.id " +
                        "JOIN address addr ON p.address_id = addr.id " +
                        "WHERE  " +
                        "    gts.name = ? ",
                filterByFio.getText(), filterByAts.getValue(), filterByDistrict.getText()
        );

        query += "GROUP BY " +
                "    s.last_name " +
                "ORDER BY  " +
                "    s.last_name;";

        executeQuery(query, SubscribersTable, comboBoxGts.getValue(), filterByFio.getText(), filterByAts.getValue(), filterByDistrict.getText());
    }

    @FXML
    protected void showFilterPopUp() {
        SubscribersFilterPanel.setVisible(true);
    }

    @FXML
    protected void hideFilterPopUp() {
        SubscribersFilterPanel.setVisible(false);
    }
}
