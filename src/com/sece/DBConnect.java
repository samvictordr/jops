package com.sece;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static final String URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String USER = "root";
    private static final String PW = "rootpassword";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PW);
    }
}
