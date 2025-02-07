package com.example.application.database;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection
{
    // Einzelne Parameter für die Datenbankverbindung
    private static final String HOST = "162.55.45.4";
    private static final int PORT = 3306;
    private static final String DATABASE = "backlog";
    private static final String USER = "backlog";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws SQLException {
        // Erzeugen und Konfigurieren der DataSource
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(HOST);
        dataSource.setPortNumber(PORT);
        dataSource.setDatabaseName(DATABASE);
        dataSource.setUser(USER);
        dataSource.setPassword(PASSWORD);

        // Optional: Zusätzliche Parameter setzen
        dataSource.setUseSSL(false);           // SSL deaktivieren, falls nicht benötigt
        // dataSource.setServerTimezone("UTC"); // Zeitzone festlegen, falls erforderlich

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Aufbau der Datenbankverbindung", e);
        }
    }

}
