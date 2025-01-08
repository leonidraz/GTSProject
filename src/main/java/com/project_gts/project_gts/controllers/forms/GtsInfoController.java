package com.project_gts.project_gts.controllers.forms;

import com.project_gts.project_gts.controllers.data.DatabaseConnection;
import com.project_gts.project_gts.controllers.data.TableViewUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.sql.*;

public class GtsInfoController {
    @FXML
    private Label labelTotalSubscribers;

    @FXML
    private TableView<ObservableList<Object>> tableAtcMini;

    @FXML
    private Label percentageRegularSubscribers;

    @FXML
    private Label percentagePrivilegedSubscribers;

    @FXML
    private Label nameGtsInfo;

    @FXML
    private Label cityGtsInfo;

    @FXML
    private Label numberDebtors;

    @FXML
    private AnchorPane ListSubscribersPopUp;

    @FXML
    private TableView<ObservableList<Object>> tableSubscribersMini;

    @FXML
    private Label MainPopUpTitle;

    @FXML
    private TableView<ObservableList<Object>> tableDebtorsMini;

    @FXML
    private ComboBox<String> comboBoxGts;

    public void setComboBox(ComboBox<String> comboBox) {
        this.comboBoxGts = comboBox;

        this.comboBoxGts.setOnAction(event -> {
            String selectedGtsName = comboBoxGts.getValue();
            if (selectedGtsName != null && !selectedGtsName.isEmpty()) {
                updateGtsInfo(selectedGtsName);
            }
        });

        if (comboBox.getValue() != null){
            updateGtsInfo(comboBox.getValue());
        }

    }

    private void updateGtsInfo(String gtsName) {
        String subscriberCountTotalQuery = "SELECT COUNT(DISTINCT s.id) " +
                "FROM gts g " +
                "JOIN ats a ON a.gts_id = g.id " +
                "JOIN phones p ON p.ats_id = a.id " +
                "JOIN subscribers s ON p.subscriber_id = s.id " +
                "WHERE g.name = ?";

        String atsInfoQuery = "SELECT a.id, a.name, a.type, a.district " +
                "FROM ats a " +
                "JOIN gts g ON a.gts_id = g.id " +
                "WHERE g.name = ?";

        String subscribersRatioByTypeQuery = "SELECT " +
                "    ROUND(COUNT(CASE WHEN s.type = 'обычный' THEN 1 END) * 100.0 / COUNT(s.id)) AS regular_percentage, " +
                "    ROUND(COUNT(CASE WHEN s.type = 'льготник' THEN 1 END) * 100.0 / COUNT(s.id)) AS privileged_percentage " +
                "FROM subscribers s " +
                "JOIN phones p ON s.id = p.subscriber_id " +
                "JOIN ats a ON p.ats_id = a.id " +
                "JOIN gts g ON a.gts_id = g.id " +
                "WHERE g.name = ?";

        String gtsInfoQuery = "SELECT " +
                "g.name, " +
                "g.city " +
                "FROM " +
                "gts g " +
                "WHERE  " +
                "g.name = ?";

        String countDebtorsQuery = "SELECT COUNT(DISTINCT s.id) AS debtor_count " +
                "FROM subscribers s " +
                "JOIN receipts r ON s.id = r.subscriber_id " +
                "JOIN phones p ON s.id = p.subscriber_id " +
                "JOIN ats a ON p.ats_id = a.id " +
                "JOIN gts g ON a.gts_id = g.id " +
                "WHERE r.status != 'Оплачено' " +
                "  AND g.name = ?";

        String listSubscribersByGts = "SELECT DISTINCT s.* " +
                "FROM subscribers s " +
                "JOIN phones p ON p.subscriber_id = s.id " +
                "JOIN ats a ON p.ats_id = a.id " +
                "JOIN gts g ON a.gts_id = g.id " +
                "WHERE g.name = ?";

        String listDebtorsByGts = "SELECT  " +
                "    s.id, " +
                " s.last_name, " +
                "    s.first_name, " +
                "    s.middle_name, " +
                "    r.total_to_pay AS total_amount, " +
                "    COALESCE(p.total_paid, 0) AS total_paid, " +
                "    (r.total_to_pay - COALESCE(p.total_paid, 0)) AS outstanding_balance " +
                "FROM subscribers s " +
                "JOIN receipts r ON s.id = r.subscriber_id " +
                "LEFT JOIN ( " +
                "    SELECT  " +
                "        p.receipt_id, " +
                "        SUM(p.payment_amount) AS total_paid " +
                "    FROM payments p " +
                "    GROUP BY p.receipt_id " +
                ") p ON r.id = p.receipt_id " +
                "JOIN phones ph ON s.id = ph.subscriber_id " +
                "JOIN ats a ON ph.ats_id = a.id " +
                "JOIN gts g ON a.gts_id = g.id " +
                "WHERE r.status != 'Оплачено' AND g.name = ? " +
                "GROUP BY  " +
                "    s.id, s.first_name, s.last_name, s.middle_name, r.id, r.total_to_pay, p.total_paid\n" +
                "ORDER BY s.id;";

        try (Connection connection = DatabaseConnection.getConnectionDB();
             PreparedStatement subscriberStatement = connection.prepareStatement(subscriberCountTotalQuery);
             PreparedStatement atsStatement = connection.prepareStatement(atsInfoQuery);
             PreparedStatement typeStatement = connection.prepareStatement(subscribersRatioByTypeQuery);
             PreparedStatement gtsInfoStatement = connection.prepareStatement(gtsInfoQuery);
             PreparedStatement countDebtorsStatement = connection.prepareStatement(countDebtorsQuery);
             PreparedStatement listSubscribersStatement = connection.prepareStatement(listSubscribersByGts);
             PreparedStatement listDebtorsStatement = connection.prepareStatement(listDebtorsByGts)) {

            subscriberStatement.setString(1, gtsName);
            try (ResultSet rs = subscriberStatement.executeQuery()) {
                if (rs.next()) {
                    int totalSubscribers = rs.getInt(1);
                    labelTotalSubscribers.setText(totalSubscribers + " users");
                }
            }

            atsStatement.setString(1, gtsName);
            TableViewUtil.populateTable(tableAtcMini, atsStatement, connection);

            typeStatement.setString(1, gtsName);
            try(ResultSet rs = typeStatement.executeQuery()){
                if (rs.next()){
                    int regularSubscriberPercentage = rs.getInt("regular_percentage");
                    int privilegedSubscriberPercentage = rs.getInt("privileged_percentage");

                    percentageRegularSubscribers.setText(regularSubscriberPercentage + "%");
                    percentagePrivilegedSubscribers.setText(privilegedSubscriberPercentage + "%");
                }
            }

            gtsInfoStatement.setString(1, gtsName);
            try (ResultSet rs = gtsInfoStatement.executeQuery()){
                if (rs.next()){
                    String nameGts = rs.getString("name").replace("ГТС", "").trim();
                    String cityGts = rs.getString("city");

                    nameGtsInfo.setText("ГТС\n" + nameGts);
                    cityGtsInfo.setText(cityGts);
                }
            }

            countDebtorsStatement.setString(1, gtsName);
            try (ResultSet rs = countDebtorsStatement.executeQuery()){
                if (rs.next()){
                    String countDebtors = rs.getString("debtor_count");

                    numberDebtors.setText(countDebtors);
                }
            }

            listSubscribersStatement.setString(1, gtsName);
            TableViewUtil.populateTable(tableSubscribersMini, listSubscribersStatement, connection);

            listDebtorsStatement.setString(1, gtsName);
            TableViewUtil.populateTable(tableDebtorsMini, listDebtorsStatement, connection);

            DatabaseConnection.closeConnectionDB();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void showListSubscribersPopUp() {
        MainPopUpTitle.setText("Абоненты");
        ListSubscribersPopUp.setVisible(true);
        tableSubscribersMini.setVisible(true);
    }

    @FXML
    protected void hideListSubscribersPopUp() {
        ListSubscribersPopUp.setVisible(false);
        tableSubscribersMini.setVisible(false);
        tableDebtorsMini.setVisible(false);
    }

    @FXML
    protected void showDebtorsPopUp() {
        MainPopUpTitle.setText("Должники");
        ListSubscribersPopUp.setVisible(true);
        tableDebtorsMini.setVisible(true);
    }
}
