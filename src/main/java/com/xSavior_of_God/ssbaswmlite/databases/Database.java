package com.xSavior_of_God.ssbaswmlite.databases;
/**
 * Developed by xSavior_of_God
 * Since 01-07-2022
 *
 * For SUPPORT
 *  <Telegram/>    @xSavior_of_God
 *  <Discord/>     https://discord.gg/5UuVdTE
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {
    public abstract Connection getConnection();

    public abstract void connect(String DB_URL, String DB_DRIVER, String USER, String PASS);

    public abstract Boolean checkConnection();

    public abstract ResultSet query(String query, Statement stmt);

    public abstract void disconnect();

    public abstract void update(String query);

    public abstract void checktables() throws SQLException;
}
