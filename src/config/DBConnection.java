    package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=DentalClinicDB;encrypt=false";
            String user = "sa";
            String pass = "123";

            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}