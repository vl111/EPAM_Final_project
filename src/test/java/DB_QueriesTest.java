import controller.Database.DB_Queries;
import controller.buspark.Buspark;
import controller.resource_loader.ResourceLoader;
import model.Bus;
import model.Driver;
import model.Route;

import java.sql.*;

public class DB_QueriesTest {
    public final String NAME = ResourceLoader.getProperyByKey("DB_Name").toString();
    public final String PASSWORD = ResourceLoader.getProperyByKey("DB_Password").toString();
    public final String CONNECTION_URL = ResourceLoader.getProperyByKey("connectionURL").toString();
    public final String testBusName = "testBusName", testDriverName = "testDriverName",
            testRouteName = "testRouteName";
    public Connection conn;
    public Statement statement;
    public Buspark buspark;
    public Driver d;
    public Bus b;
    public Route r;
    DB_Queries dbq;

    public void removeDriverByName(String name) {
        openConnection();
        try {
            statement.executeUpdate("delete from drivers " +
                    " where driv_name = '" + name + "';");
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void removeBusByName(String name) {
        openConnection();
        try {
            statement.executeUpdate("delete from buses " +
                    " where bus_name = '" + name + "';");
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void removeRouteByName(String name) {
        openConnection();
        try {
            statement.executeUpdate("delete from routes " +
                    " where route_name = '" + name + "';");
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public Driver getDriverByName(String name) {
        openConnection();
        Driver driver = null;
        ResultSet result;
        try {
            result = statement.executeQuery("select id, driv_name, bus_id," +
                    " route_confirmed  from drivers " +
                    " where driv_name = '" + name + "';");
            conn.commit();
            if (result.next()) {
                driver = new Driver(result.getLong(1), result.getString(2),
                        result.getLong(3), result.getBoolean(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
            return driver;
        }
    }

    public Bus getBusByName(String name) {
        openConnection();
        Bus bus = null;
        ResultSet result;
        try {
            result = statement.executeQuery("select id, bus_name, route_id from buses " +
                    " where bus_name = '" + name + "';");
            conn.commit();
            if (result.next()) {
                bus = new Bus(result.getLong(1), result.getString(2),
                        result.getLong(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
            return bus;
        }
    }

    public void openConnection() {
        try {
            conn = DriverManager.getConnection(CONNECTION_URL, NAME, PASSWORD);
            conn.setAutoCommit(false);
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            conn.close();
            statement.close();
            conn = null;
            statement = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
