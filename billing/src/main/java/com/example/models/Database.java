package com.example.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:lec_billing.db";

    // Create or connect to the database
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Initialize tables automatically on startup
    public static void initialize() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            // ✅ Create users table if not exists
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL
                );
            """);

            // ✅ Create customers table if not exists
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                    id TEXT PRIMARY KEY,
                    name TEXT,
                    address TEXT,
                    meterNumber TEXT,
                    usage REAL,
                    lastBill REAL
                );
            """);

            System.out.println("✅ Database initialized successfully.");
        } catch (Exception e) {
            System.err.println("❌ Database initialization failed:");
            e.printStackTrace();
        }
    }
}
