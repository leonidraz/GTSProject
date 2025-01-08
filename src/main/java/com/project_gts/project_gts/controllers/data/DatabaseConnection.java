package com.project_gts.project_gts.controllers.data;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static Connection connection = null;

    public static Connection getConnectionDB() {
        if (connection == null){
            connection = createConnectionDB();
        }
        return connection;
    }

    private static Connection createConnectionDB(){
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null){
                System.out.println("Файл config.properties не найден!");
            }
            else {
                Properties properties = new Properties();
                properties.load(input);

                String dbUrl = properties.getProperty("db.url");
                String dbUserName = properties.getProperty("db.username");
                String dbPassword = properties.getProperty("db.password");

                return DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnectionDB() {
        if (connection != null){
            try {
                connection.close();
                connection = null;
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

}
