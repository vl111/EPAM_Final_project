package controller.Database;

import controller.resource_loader.ResourceLoader;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DB_Connection {
    private final static Logger LOG = Logger.getLogger(DB_Queries.class.getSimpleName());

    private final String NAME = ResourceLoader.getProperyByKey("DB_Name").toString();
    private final String PASSWORD = ResourceLoader.getProperyByKey("DB_Password").toString();
    private final String CONNECTION_URL = ResourceLoader.getProperyByKey("connectionURL").toString();

    private volatile Connection conn;
    private volatile Statement statement;

    public DB_Connection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//com.mysql.jdbc.Driver
            LOG.info("SQL connector imported.");
        } catch (ClassNotFoundException e) {
            LOG.error("Class not found.");
            e.printStackTrace();
        }
    }

    protected void openConnection() {
        try {
            if (conn == null || statement == null) {
                conn = DriverManager.getConnection(CONNECTION_URL, NAME, PASSWORD);
                conn.setAutoCommit(false);
                statement = conn.createStatement();
                LOG.info("Connected to database.");

            }
        } catch (SQLException e) {
            LOG.error("Error on opening connection.");
            e.printStackTrace();
        }
    }

    protected void closeConnection() {
        try {
            conn.close();
            statement.close();
            conn = null;
            statement = null;
            LOG.info("Database connection closed.");
        } catch (SQLException e) {
            LOG.error("Error on closing connection.");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public Statement getStatement() {
        return statement;
    }
}