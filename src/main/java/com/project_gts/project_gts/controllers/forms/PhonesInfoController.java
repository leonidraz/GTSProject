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

public class PhonesInfoController {

    @FXML
    private ComboBox<String> comboBoxGts;

    @FXML
    private Label totalEntries;

    @FXML
    private TableView<ObservableList<Object>> PhonesTable;

    @FXML
    private TextField filterByAddress;

    @FXML
    private TextField filterByNumer;

    @FXML
    private ComboBox<String> filterByAts;

    @FXML
    private Label NumerLabel;

    @FXML
    private Label AddressLabel;

    @FXML
    private Label atsNameLabel;

    @FXML
    private AnchorPane PhonesFilterPanel;

    @FXML
    private CheckBox cityCheckbox;

    @FXML
    private CheckBox departmentCheckbox;

    @FXML
    private CheckBox institutionCheckbox;

    @FXML
    private CheckBox mainPhoneCheckbox;

    @FXML
    private CheckBox parallelPhoneCheckbox;

    @FXML
    private CheckBox pairedPhoneCheckbox;

    @FXML
    private CheckBox publicPhoneCheckbox;

    @FXML
    private CheckBox payPhoneCheckbox;

    @FXML
    private RadioButton intercityYesRadioButton;

    @FXML
    private RadioButton intercityNoRadioButton;

    @FXML
    private RadioButton freeYesRadioButton;

    @FXML
    private RadioButton freeNoRadioButton;

    @FXML
    private ToggleGroup intercityToggleGroup;

    @FXML
    private ToggleGroup freePhonesToggleGroup;

    public void setComboBox(ComboBox<String> comboBox) {
        this.comboBoxGts = comboBox;

        this.comboBoxGts.setOnAction(event -> {
            String selectedGtsName = comboBoxGts.getValue();
            if (selectedGtsName != null && !selectedGtsName.isEmpty()) {
                loadAtsComboBox(selectedGtsName);
                updatePhonesInfo(selectedGtsName, filterByNumer.getText(), filterByAts.getValue(), filterByAddress.getText());
            }
        });

        if (comboBox.getValue() != null) {
            loadAtsComboBox(comboBox.getValue());
            updatePhonesInfo(comboBoxGts.getValue(), filterByNumer.getText(), filterByAts.getValue(), filterByAddress.getText());
        }
    }

    @FXML
    private void initialize() {
        addFilterListener(filterByNumer, NumerLabel, "Номер");
        addFilterListener(filterByAddress, AddressLabel, "Адрес");

        filterByAts.valueProperty().addListener((observable, oldValue, newValue) -> {
            updatePhonesInfo(comboBoxGts.getValue(), getFilterValue(filterByNumer), newValue, getFilterValue(filterByAddress));
            if (filterByAts.getValue() != null && !filterByAts.getValue().isEmpty()) {
                atsNameLabel.setVisible(false);
            } else {
                atsNameLabel.setVisible(true);
            }
        });

        intercityToggleGroup = new ToggleGroup();
        freePhonesToggleGroup = new ToggleGroup();

        // Присваиваем ToggleGroup каждой из радиокнопок
        intercityYesRadioButton.setToggleGroup(intercityToggleGroup);
        intercityNoRadioButton.setToggleGroup(intercityToggleGroup);
        freeYesRadioButton.setToggleGroup(freePhonesToggleGroup);
        freeNoRadioButton.setToggleGroup(freePhonesToggleGroup);

        cityCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updatePhonesInfo(comboBoxGts.getValue(), getFilterValue(filterByNumer), filterByAts.getValue(), getFilterValue(filterByAddress));
        });
        departmentCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updatePhonesInfo(comboBoxGts.getValue(), getFilterValue(filterByNumer), filterByAts.getValue(), getFilterValue(filterByAddress));
        });
        institutionCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updatePhonesInfo(comboBoxGts.getValue(), getFilterValue(filterByNumer), filterByAts.getValue(), getFilterValue(filterByAddress));
        });
        mainPhoneCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updatePhonesInfo(comboBoxGts.getValue(), getFilterValue(filterByNumer), filterByAts.getValue(), getFilterValue(filterByAddress));
        });
        parallelPhoneCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updatePhonesInfo(comboBoxGts.getValue(), getFilterValue(filterByNumer), filterByAts.getValue(), getFilterValue(filterByAddress));
        });

        pairedPhoneCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updatePhonesInfo(comboBoxGts.getValue(), getFilterValue(filterByNumer), filterByAts.getValue(), getFilterValue(filterByAddress));
        });
        publicPhoneCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updatePhonesInfo(comboBoxGts.getValue(), getFilterValue(filterByNumer), filterByAts.getValue(), getFilterValue(filterByAddress));
        });
        payPhoneCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updatePhonesInfo(comboBoxGts.getValue(), getFilterValue(filterByNumer), filterByAts.getValue(), getFilterValue(filterByAddress));
        });
        intercityToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            updatePhonesInfo(comboBoxGts.getValue(), getFilterValue(filterByNumer), filterByAts.getValue(), getFilterValue(filterByAddress));
        });

        freePhonesToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            updatePhonesInfo(comboBoxGts.getValue(), getFilterValue(filterByNumer), filterByAts.getValue(), getFilterValue(filterByAddress));
        });
    }

    private void addFilterListener(TextField filterField, Label label, String labelText) {
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedGtsName = comboBoxGts.getValue();
            if (selectedGtsName != null && !selectedGtsName.isEmpty()) {
                updatePhonesInfo(selectedGtsName, getFilterValue(filterByNumer), filterByAts.getValue(), getFilterValue(filterByAddress));
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


    private void updatePhonesInfo(String gtsName, String NumerFilter, String atsFilter, String AddressFilter) {
        String query = buildQuery(
                "SELECT  " +
                        "    p.id, " +
                        "    p.number, " +
                        "    p.type, " +
                        "    s.last_name, " +
                        "    s.first_name, " +
                        "    s.middle_name, " +
                        "    s.intercity, " +
                        "    COALESCE( " +
                        "    addr.district || ' район, ' || addr.street || ', д.' || addr.building ||  " +
                        "    COALESCE(', кв.' || addr.apartment, ''),  " +
                        "    'Адрес не указан' " +
                        ") AS full_address  " +
                        "FROM  " +
                        "    phones p " +
                        "LEFT JOIN  " +
                        "    subscribers s ON p.subscriber_id = s.id " +
                        "LEFT JOIN  " +
                        "    ats ON p.ats_id = ats.id " +
                        "LEFT JOIN  " +
                        "    gts g ON ats.gts_id = g.id " +
                        "LEFT JOIN  " +
                        "    address addr ON p.address_id = addr.id " +
                        "WHERE  " +
                        "    g.name = ? ",
                NumerFilter, atsFilter, AddressFilter
        );

        query += " ORDER BY p.number ASC;";

        executeQuery(query, PhonesTable, gtsName, NumerFilter, atsFilter, AddressFilter);
        totalEntries.setText("Всего записей: " + PhonesTable.getItems().size());
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

        if (publicPhoneCheckbox.isSelected()) {
            phoneTypeFilter.append("p.type = 'Общественный' OR ");
        }

        if (payPhoneCheckbox.isSelected()) {
            phoneTypeFilter.append("p.type = 'Таксофон' OR ");
        }

        if (phoneTypeFilter.length() > 0) {
            phoneTypeFilter.setLength(phoneTypeFilter.length() - 4);
        } else {
            phoneTypeFilter.append("1=1");
        }

        return phoneTypeFilter.toString();
    }

    private String getIntercityFilter() {
        if (intercityYesRadioButton.isSelected()) {
            return "s.intercity = TRUE";
        } else if (intercityNoRadioButton.isSelected()) {
            return "s.intercity = FALSE";
        }
        return "1=1";
    }

    private String getFreePhonesFilter() {
        if (freeYesRadioButton.isSelected()) {
            return "p.subscriber_id IS NULL";
        } else if (freeNoRadioButton.isSelected()) {
            return "p.subscriber_id IS NOT NULL";
        }
        return "1=1";
    }

    private String buildQuery(String baseQuery, String numberFilter, String atsFilter, String addressFilter) {
        // Фильтр по номеру телефона
        if (numberFilter != null && !numberFilter.isEmpty()) {
            baseQuery += " AND p.number LIKE ?";
        }

        // Фильтр по имени АТС
        if (atsFilter != null && !atsFilter.isEmpty()) {
            baseQuery += " AND ats.name = ?";
        }

        // Фильтр по адресу
        if (addressFilter != null && !addressFilter.isEmpty()) {
            baseQuery += " AND CONCAT(addr.district, ' ', addr.street, ' ', addr.building, ' ', addr.apartment) LIKE ?";
        }

        String atsTypeFilter = getAtsTypeFilter();
        if (!atsTypeFilter.isEmpty()) {
            baseQuery += " AND (" + atsTypeFilter + ")";
        }

        String phoneTypeFilter = getPhoneTypeFilter();
        if (!phoneTypeFilter.isEmpty()) {
            baseQuery += " AND (" + phoneTypeFilter + ")";
        }

        String intercityFilter = getIntercityFilter();
        if (!intercityFilter.isEmpty()) {
            baseQuery += " AND " + intercityFilter;
        }
        String freePhonesFilter = getFreePhonesFilter();
        if (!freePhonesFilter.isEmpty()) {
            baseQuery += " AND " + freePhonesFilter;
        }
        return baseQuery;
    }

    private void executeQuery(String query, TableView<ObservableList<Object>> tableView, String gtsName, String  NumerFilter, String atsFilter, String AddressFilter) {
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
            if (AddressFilter != null && !AddressFilter.isEmpty()) {
                statement.setString(paramIndex++, "%" + AddressFilter + "%");
            }

            TableViewUtil.populateTable(tableView, statement, connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnectionDB();
        }
    }

    @FXML
    private void handleInfoSubscribersButtonClick(){
        String query = buildQuery(
                "SELECT  " +
                        "    s.id," +
                        "    s.last_name,  " +
                        "    s.first_name,  " +
                        "    s.middle_name,  " +
                        "    s.gender, " +
                        "    s.age, " +
                        "    s.type AS subscriber_type, " +
                        "    p.type AS phone_type, " +
                        "    ats.name AS ats_name,  " +
                        "    COALESCE(addr.district || ' район, ' || addr.street || ', д.' || addr.building || ', кв.' || addr.apartment, 'Адрес не указан') AS full_address  " +
                        "FROM  " +
                        "    phones p  " +
                        "LEFT JOIN  " +
                        "    subscribers s ON p.subscriber_id = s.id  " +
                        "LEFT JOIN  " +
                        "    ats ON p.ats_id = ats.id  " +
                        "LEFT JOIN  " +
                        "    address addr ON p.address_id = addr.id  " +
                        "LEFT JOIN  " +
                        "    gts ON ats.gts_id = gts.id " +
                        "WHERE  " +
                        "   gts.name = ? ",
                filterByNumer.getText(), filterByAts.getValue(), filterByAddress.getText()
        );

        executeQuery(query, PhonesTable, comboBoxGts.getValue(), filterByNumer.getText(), filterByAts.getValue(), filterByAddress.getText());
    }

    @FXML
    protected void showFilterPopUp() {
        PhonesFilterPanel.setVisible(true);
    }

    @FXML
    protected void hideFilterPopUp() {
        PhonesFilterPanel.setVisible(false);
    }
}
