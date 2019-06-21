import controller.Database.DB_Queries;
import controller.ResourceLoader;
import controller.buspark.Buspark;
import model.Bus;
import model.Driver;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Order;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test_DB_QueriesSignUnsignDriverBus {

    private final String NAME = ResourceLoader.getProperties().get("DB_Name").toString();
    private final String PASSWORD = ResourceLoader.getProperties().get("DB_Password").toString();
    private final String CONNECTION_URL = ResourceLoader.getProperties().get("connectionURL").toString();
    private final String testBusName = "testBusName", testDriverName = "testDriverName";
    DB_Queries dbq;
    private Connection conn;
    private Statement statement;
    private Buspark buspark;
    private Driver d;
    private Bus b;

    @Before
    public void setup() {
        dbq = DB_Queries.getInstance();
        buspark = Buspark.getInstance();
        if (d == null)
            dbq.addDriver(testDriverName, "testDriverPassword");
        d = getDriverByName(testDriverName);
        if (b == null)
            dbq.addBus(testBusName);
        b = getBusByName(testBusName);
    }


    @After
    public void after() {
        removeBusByName(testBusName);
        removeDriverByName(testDriverName);
        System.out.println("After");
    }


    @Test
    @Ignore
    @Order(1)
    public void testAddTables() {
        dbq.addTables();
    }

    @Test
    @Order(2)
    public void testSignUnsignDriverBus1() {
        buspark.signUnsign(d, b);
        d = getDriverByName(testDriverName);
        assertEquals(true, d.getBusId() > 0);
        buspark.signUnsign(d, b);
        d = getDriverByName(testDriverName);
        assertEquals(false, d.getBusId() > 0);
    }

    private void removeDriverByName(String name) {
        openConnection();
        ResultSet result;
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

    private void removeBusByName(String name) {
        openConnection();
        ResultSet result;
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

    private Driver getDriverByName(String name) {
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

    private Bus getBusByName(String name) {
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

    private void openConnection() {
        try {
            conn = DriverManager.getConnection(CONNECTION_URL, NAME, PASSWORD);
            conn.setAutoCommit(false);
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
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