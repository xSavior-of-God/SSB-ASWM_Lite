package com.xSavior_of_God.ssbaswmlite.databases.sql;
/**
 * Developed by xSavior_of_God
 * Since 01-07-2022
 *
 * For SUPPORT
 *  <Telegram/>    @xSavior_of_God
 *  <Discord/>     https://discord.gg/5UuVdTE
 */

import com.xSavior_of_God.ssbaswmlite.SSBASWMLite;
import com.xSavior_of_God.ssbaswmlite.databases.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;

public class SQLite extends Database {
    private Connection conn = null;
    String url = null;

    @Override
    public Connection getConnection() {
        return conn;
    }

    @Override
    public void connect(String DB_URL, String DB_DRIVER, String USER, String PASS) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            return;
        }

        if (SSBASWMLite.instance.getDataFolder().getAbsolutePath().contains("/"))
            url = "jdbc:sqlite:" + SSBASWMLite.instance.getDataFolder().getAbsolutePath() + "/database.db";
        else
            url = "jdbc:sqlite:" + SSBASWMLite.instance.getDataFolder().getAbsolutePath() + "\\database.db";

        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSQLite Database Connected!"));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6SQLite Database Start Table Check!"));
                checktables();
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSQLite Database Table Check COMPLETED!"));

            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4HEY! &7I was unable to connect to the database (SQLite), check your configuration"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean checkConnection() {
        try {
            if (conn.isClosed()) {
                conn.close();
                conn = null;
                conn = DriverManager.getConnection(url);
            }
        } catch (SQLException e) {
            try {
                conn.close();
                conn = DriverManager.getConnection(url);
                if (!conn.isValid(5)) {
                    return false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public ResultSet query(String query, Statement stmt) {
        try {
            if (conn.isClosed()) {
                conn.close();
                conn = null;
                conn = DriverManager.getConnection(url);
                if (stmt != null) {
                    stmt.close();
                    stmt = conn.createStatement();
                }
            }
            final ResultSet result = stmt.executeQuery(query);

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String query) {
        if (!checkConnection()) {
            try {
                throw new Exception("An error has occurred, Cannot connect to the database!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            stmt = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checktables() throws SQLException {
        if (!checkConnection()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTable lastLogoutLocations&a Exist!"));
            return;
        }

        // Check Table LastLogoutLocation
        final DatabaseMetaData meta = conn.getMetaData();
        ResultSet tables;
        tables = meta.getTables(null, null, "lastLogoutLocations", null);
        if (!tables.next()) {
            update("CREATE TABLE `lastLogoutLocations` ("
                + "`Username` VARCHAR(16) NOT NULL, "
                + "`UUID` VARCHAR(32) DEFAULT NULL, "
                + "`world` VARCHAR(255) NOT NULL, "
                + "`x` VARCHAR(255) NOT NULL, "
                + "`y` VARCHAR(255) NOT NULL, "
                + "`z` VARCHAR(255) NOT NULL, "
                + "`yaw` VARCHAR(255) NOT NULL, "
                + "`pitch` VARCHAR(255) NOT NULL, "
                + "PRIMARY KEY (`UUID`) );");
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTable lastLogoutLocations&a Created!"));
        }

    }
}
