package com.project_gts.project_gts.controllers.data;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.*;

public class TableViewUtil {

    public static void populateTable(TableView<ObservableList<Object>> tableView, PreparedStatement preparedStatement, Connection connection) throws Exception {
        tableView.getColumns().clear();
        tableView.getItems().clear();

        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();

        int columnCount = metaData.getColumnCount();

        // Добавляем колонки в таблицу
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            TableColumn<ObservableList<Object>, Object> column = new TableColumn<>(columnName);
            final int columnIndex = i - 1;

            column.setCellValueFactory(param ->
                    new SimpleObjectProperty<>(param.getValue().get(columnIndex))
            );

            if (tableView.getId().equals("tableAtcMini")) {
                if (columnName.equals("id")) {
                    column.setMaxWidth(50);
                } else {
                    column.setMaxWidth(150);
                }
            }

            tableView.getColumns().add(column);
        }

        // Добавляем данные в таблицу
        ObservableList<ObservableList<Object>> data = FXCollections.observableArrayList();
        while (resultSet.next()) {
            ObservableList<Object> row = FXCollections.observableArrayList();
            for (int i = 1; i <= columnCount; i++) {
                row.add(resultSet.getObject(i));
            }
            data.add(row);
        }

        tableView.setItems(data);

        resultSet.close();
    }


}
