package com.readutf.mauth.database.impl;

import com.readutf.mauth.database.Database;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class MySQL implements Database {


    Connection connection = null;
    Statement statement;


    String host, database, username, password;
    int port;

    public MySQL(String host, String database, String username, String password, int port) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public boolean connect() {
        Bukkit.getLogger().fine("Opening MYSQL Connection");
        try {
            if(connection != null && !connection.isClosed()) {
                return true;
            }

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://"
                            + host + ":" + port + "/" + database,
                    username, password);

            statement = connection.createStatement();
            createTable("mauth", "uuid", "ip");
            return true;
        } catch (Exception e) {
            Bukkit.getLogger().severe("Unable to connect.");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getPreviousIp(UUID uuid) {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mauth WHERE UUID = '" + uuid.toString() + "';");
            while (resultSet.next()) {
                return resultSet.getString("ip");
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public void setPreviousIp(UUID uuid, String ip) {
        try {
            if(isSet(uuid)) {
                connection.prepareStatement("UPDATE mauth SET ip = '" + ip + "' WHERE uuid = '" + uuid.toString() + "'").executeUpdate();
                return;
            }
            connection.prepareStatement("INSERT INTO mauth (uuid, ip) VALUES ('" + uuid.toString() + "', '" + ip + "')").executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isSet(UUID uuid) {
        return getPreviousIp(uuid) != null;
    }

    @Override
    public void removeIp(UUID uuid) {
        try {
            connection.prepareStatement("DELETE FROM mauth WHERE uuid = '" + uuid.toString() + "'").executeUpdate();
        } catch (SQLException throwables) {
        }
    }

    public void createTable(String table, String row1, String row2) {
        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " (" + row1 + " varchar(255), " + row2 + " varchar(255))").executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
