package org.example;

import java.sql.*;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        //createDatabase();
        //createTable();
    }

    public static void createDatabase() {
        final String DB_URL = "jdbc:mariadb://localhost/";
        final String USER = "root";
        final String PASSWORD = "123456";

        Connection connection = null;
        Statement statement = null;

        try {
            // Step 1: Register JDBC driver (only required in older JDBC versions)
            // Class.forName("com.mysql.jdbc.Driver");

            // Step 2: Open a connection
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // Step 3: Create a statement
            statement = connection.createStatement();

            Scanner in = new Scanner(System.in);
            System.out.println("Вкажіть назву БД:");
            String dbName =in.nextLine();

            // Step 4: Execute SQL query to create a database
            String sql = "CREATE DATABASE "+dbName;
            statement.executeUpdate(sql);
            System.out.println("Database created successfully.");

        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } finally {
            // Step 5: Close resources
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void createTable() {
        final String DB_URL = "jdbc:mariadb://localhost/salo";
        final String USER = "root";
        final String PASSWORD = "123456";

        Connection connection = null;
        Statement statement = null;

        try {
            // Step 1: Register JDBC driver (only required in older JDBC versions)
            // Class.forName("com.mysql.jdbc.Driver");

            // Step 2: Open a connection
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // Step 3: Create a statement
            statement = connection.createStatement();

            // Step 4: Execute SQL query to create a table
            String sql = "CREATE TABLE user (" +
                    "id INT NOT NULL AUTO_INCREMENT," +
                    "username VARCHAR(50) NOT NULL," +
                    "email VARCHAR(100) NOT NULL," +
                    "PRIMARY KEY (id)" +
                    ")";
            statement.executeUpdate(sql);
            System.out.println("Table 'user' created successfully.");

        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } finally {
            // Step 5: Close resources
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

}