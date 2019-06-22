package controller.Database;

import controller.resource_loader.ResourceLoader;
import model.Driver;
import model.*;
import org.apache.log4j.Logger;

import java.sql.*;

/*This class contains all the queries to SQL database.
 * */

public class DB_Queries {
    private final static Logger LOG = Logger.getLogger(DB_Queries.class.getSimpleName());

    private static volatile DB_Queries instance = null;
    private final String NAME = ResourceLoader.getProperties().get("DB_Name").toString();
    private final String PASSWORD = ResourceLoader.getProperties().get("DB_Password").toString();
    private final String CONNECTION_URL = ResourceLoader.getProperties().get("connectionURL").toString();

    private volatile Connection conn;
    private volatile Statement statement;

    private DB_Queries() {
        synchronized (DB_Queries.class) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");//com.mysql.jdbc.Driver
                LOG.info("SQL connector imported.");
            } catch (ClassNotFoundException e) {
                LOG.error("Class not found.");
                e.printStackTrace();
            }

        }
    }

    public static DB_Queries getInstance() {
        if (instance == null)
            synchronized (DB_Queries.class) {
                if (instance == null) {
                    instance = new DB_Queries();
                    LOG.info("New instance of " + DB_Queries.class.getSimpleName()
                            + " created.");
                }
            }
        return instance;
    }

    public void addDriver(String name, String password) {
        synchronized (instance) {
            openConnection();
            try {
                statement.executeUpdate("INSERT INTO drivers(driv_name, password) " +
                        "VALUES ('" + name + "', '" + password + "');");
                conn.commit();
            } catch (SQLException e) {
                LOG.error("SQL exception on adding new driver.");
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    public void addAdministrator(String name, String password) {
        synchronized (instance) {
            openConnection();
            try {
                statement.executeUpdate("INSERT INTO administrators(adm_name, password) " +
                        "VALUES ('" + name + "', '" + password + "');");
                conn.commit();
                LOG.info("Administrator added");
            } catch (SQLException e) {
                LOG.error("Exception on adding administrator.");
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    public void addRoute(String name) {
        synchronized (instance) {
            openConnection();
            try {
                statement.executeUpdate("INSERT INTO routes(route_name) " +
                        "VALUES ('" + name + "');");
                conn.commit();
                LOG.info("Route added.");
            } catch (SQLException e) {
                LOG.error("Exception on adding new route.");
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }

    }

    public void addBus(String name) {
        synchronized (instance) {
            openConnection();
            try {
                statement.executeUpdate("INSERT INTO buses(bus_name) " +
                        "VALUES ('" + name + "');");
                conn.commit();
                LOG.info("Bus added.");
            } catch (SQLException e) {
                LOG.error("Exception on adding new bus.");
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    public void signDriverToBus(long driverId, long busId) {
        synchronized (instance) {
            openConnection();
            try {
                statement.executeUpdate("update drivers set bus_id = " + busId
                        + ", route_confirmed = false where id = " + driverId + ";");
                conn.commit();
                LOG.info("Driver signed to the bus.");
            } catch (SQLException e) {
                LOG.error("Exception on signing driver to the bus.");
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    public void unsignDriverFromBus(long driverId) {
        synchronized (instance) {
            openConnection();
            try {
                statement.executeUpdate("update drivers set bus_id = null" +
                        ", route_confirmed = false where id = " + driverId + ";");
                conn.commit();
                LOG.info("Driver unsigned from the bus.");
            } catch (SQLException e) {
                LOG.error("Error on unsigning driver from the bus.");
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    public void signBusToRoute(long busId, long routeId) throws SQLException {
        synchronized (instance) {
            openConnection();
            LOG.info("Creating savepoint.");
            Savepoint savepoint = conn.setSavepoint("Savepoint");
            try {
                LOG.warn("Signing bus to route.");
                statement.executeUpdate("update buses set route_id = " + routeId
                        + " where id = " + busId + ";");
                statement.executeUpdate("update drivers set " +
                        "route_confirmed = false where bus_id = " + busId + ";");
                conn.commit();
                LOG.info("Bus signed to the route.");
            } catch (SQLException e) {
                LOG.error("Exception on signing Bus to the route.");
                conn.rollback(savepoint);
                LOG.info("Rolled back to the savepoint.");
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    public void unsignBusFromRoute(long busId) throws SQLException {
        synchronized (instance) {
            openConnection();
            Savepoint savepoint = conn.setSavepoint("Savepoint");
            try {
                LOG.warn("Unsigning bus from route.");
                statement.executeUpdate("update buses set route_id = null" +
                        " where id = " + busId + ";");
                statement.executeUpdate("update drivers set " +
                        "route_confirmed = false where bus_id = " + busId + ";");
                conn.commit();
                LOG.info("Bus unsigned from the route.");
            } catch (SQLException e) {
                LOG.error("Exception on signing Bus from the route.");
                conn.rollback(savepoint);
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    //reverses the value of route_confirmed in driver table;
    public void confirmRoute(Driver driver) {
        synchronized (instance) {
            openConnection();
            try {
                String toChange = (driver.isRouteConfirmed() ? "false" : "true");
                statement.executeUpdate("update drivers set route_confirmed = " +
                        toChange + " where id = " + driver.getId() + ";");
                conn.commit();
                LOG.info("route confirmed");
            } catch (SQLException e) {
                LOG.error("Exception on confirming route.");
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    public Driver getDriverById(long id) {
        openConnection();
        Driver driver = null;
        ResultSet result;
        synchronized (instance) {
            try {
                LOG.warn("Getting driver by id.");
                result = statement.executeQuery("select d.driv_name, b.bus_name, b.id," +
                        " d.route_confirmed  from drivers d " +
                        " left join buses b on d.bus_id = b.id where d.id = " + id + ";");//, d.route_id not null
                conn.commit();
                if (result.next()) {
                    driver = new Driver(id, result.getString(1), result.getLong(3),
                            result.getBoolean(4));
                }
            } catch (SQLException e) {
                LOG.error("Error on getting driver by id.");
                e.printStackTrace();
            } finally {
                closeConnection();
                return driver;
            }
        }

    }

    public Bus getBusById(long id) {
        openConnection();
        Bus bus = null;
        ResultSet result;
        synchronized (instance) {
            try {
                LOG.warn("Getting bus by id.");
                result = statement.executeQuery("select bus_name, route_id" +
                        " from buses where id = " + id + ";");
                conn.commit();
                if (result.next()) {
                    bus = new Bus(id, result.getString(1), result.getLong(2));
                }

            } catch (SQLException e) {
                LOG.error("Error on getting bus by id");
                e.printStackTrace();
            } finally {
                closeConnection();
                return bus;
            }
        }
    }

    public Route getRouteById(long id) {
        openConnection();
        Route route = null;
        ResultSet result;
        synchronized (instance) {
            try {
                LOG.warn("Getting route by id.");
                result = statement.executeQuery("select id, route_name" +
                        " from routes where id = " + id + ";");
                conn.commit();
                if (result.next()) {
                    route = new Route(id, result.getString(2));
                }

            } catch (SQLException e) {
                LOG.error("Error on getting route by id.");
                e.printStackTrace();
            } finally {
                closeConnection();
                return route;
            }
        }
    }

    public Route getRouteByBus(long id) {
        openConnection();
        Route route = null;
        ResultSet result;
        synchronized (instance) {
            try {
                result = statement.executeQuery("select route_name" +
                        " from routes where id = " + id + ";");//, d.route_id not null
                conn.commit();
                if (result.next()) {
                    route = new Route(id, result.getString(1));
                }

            } catch (SQLException e) {
                LOG.error("Error on getting route by bus");
                e.printStackTrace();
            } finally {
                closeConnection();
                return route;
            }
        }
    }

    //Method returns logged in use in case success, and null in case fail.
    public User logInUser(long id, String pass) {
        openConnection();
        User user = null;
        ResultSet result;
        synchronized (instance) {
            try {
                LOG.warn("Logging in user.");
                result = statement.executeQuery("select id, driv_name, bus_id, route_confirmed from drivers  " +
                        "  where id = " + id + " AND password = '" + pass + "';");

                if (result.next()) {
                    user = new Driver(id, result.getString(2), result.getLong(3),
                            result.getBoolean(4));
                } else {
                    result = statement.executeQuery("select id, adm_name from administrators  " +
                            "  where id = " + id + " AND password = '" + pass + "';");
                    if (result.next()) {
                        user = new Administrator(id, result.getString(2));
                    }
                }
                LOG.info("User logged in.");
            } catch (SQLException e) {
                LOG.error("Error on log in user");
                e.printStackTrace();
            } finally {
                closeConnection();
                return user;
            }
        }

    }

    //Returns an array of drivers, where each row represents a driver
    // and elements in a row are driver`s params
    public String[][] getAllDrivers(int start, int offset) {
        openConnection();
        ResultSet result;
        String[][] drivers = new String[0][];
        synchronized (instance) {
            try {
                LOG.warn("Getting drivers.");
                result = statement.executeQuery("select d.id, d.driv_name, b.id, b.bus_name," +
                        " r.id, r.route_name, d.route_confirmed  from drivers d " +
                        " left join buses b on d.bus_id = b.id " +
                        " left join routes r on b.route_id = r.id" +
                        " LIMIT " + start + ", " + offset + ";");
                conn.commit();

                result.last();
                drivers = new String[result.getRow()][];
                result.first();
                int iter = 0;
                do {
                    drivers[iter] = new String[]{result.getString(1), result.getString(2),
                            result.getString(3), result.getString(4),
                            result.getString(5), result.getString(6),
                            result.getString(7)};
                    iter++;
                } while (result.next());

            } catch (SQLException e) {
                LOG.error("Error on getting drivers.");
                e.printStackTrace();
            } finally {
                closeConnection();
                return drivers;
            }
        }
    }

    //Returns an array of parameters of buses.
    public String[][] getAllBuses(int start, int offset) {
        openConnection();
        ResultSet result;
        String[][] buses = new String[0][];
        synchronized (instance) {
            try {
                LOG.warn("Getting all Buses.");
                result = statement.executeQuery("select  b.id, b.bus_name," +
                        " r.id, r.route_name from buses b " +
                        "  left join routes r on b.route_id = r.id" +
                        " LIMIT " + start + ", " + offset + ";");
                conn.commit();

                result.last();
                buses = new String[result.getRow()][];
                result.first();
                int iter = 0;
                do {
                    buses[iter] = new String[]{result.getString(1), result.getString(2),
                            result.getString(3), result.getString(4)};
                    iter++;
                } while (result.next());

            } catch (SQLException e) {
                LOG.error("Error on getting buses.");
                e.printStackTrace();
            } finally {
                closeConnection();
                return buses;
            }
        }
    }

    //Returns an array of parameters of routes.
    public String[][] getAllRoutes(int start, int offset) {
        openConnection();
        ResultSet result;
        String[][] routes = new String[0][];
        synchronized (instance) {
            try {
                LOG.warn("Getting routes");
                result = statement.executeQuery("select  id, route_name from routes" +
                        " LIMIT " + start + ", " + offset + ";");
                conn.commit();

                result.last();
                routes = new String[result.getRow()][];
                result.first();
                int iter = 0;
                do {
                    routes[iter] = new String[]{result.getString(1), result.getString(2)};
                    iter++;
                } while (result.next());

            } catch (SQLException e) {
                LOG.error("Error on getting all routes.");
                e.printStackTrace();
            } finally {
                closeConnection();
                return routes;
            }
        }
    }

    //This method adds four tables to DB: drivers, administrators, buses, routes.
    public void addTables() {
        synchronized (instance) {
            openConnection();
            try {
                LOG.warn("Adding tables");
                statement.executeUpdate("create table if not exists drivers(" +
                        " id integer not null primary key auto_increment," +
                        " password varchar(30) not null, driv_name varchar(25) not null," +
                        " bus_id integer references buses(id)," +
                        " route_confirmed boolean default false);");
                statement.executeUpdate("create table if not exists administrators(" +
                        " id integer not null primary key auto_increment," +
                        " password varchar(30) not null, adm_name varchar(25) not null) AUTO_INCREMENT=100000000;");
                statement.executeUpdate("create  table if not exists buses(" +
                        " id integer not null primary key auto_increment," +
                        " bus_name varchar(25) not null, " +
                        " route_id integer references routes(id));");
                statement.executeUpdate("create table if not exists routes(" +
                        " id integer not null primary key auto_increment," +
                        " route_name varchar(25) not null);");
                conn.commit();
            } catch (SQLException e) {
                LOG.error("Error on adding tables.");
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    private void openConnection() {
        try {
            if (conn == null || statement == null) {
                synchronized (instance) {
                    if (conn == null || statement == null) {
                        conn = DriverManager.getConnection(CONNECTION_URL, NAME, PASSWORD);
                        conn.setAutoCommit(false);
                        statement = conn.createStatement();
                        LOG.info("Connected to database.");
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Error on opening connection.");
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            synchronized (instance) {
                conn.close();
                statement.close();
                conn = null;
                statement = null;
                LOG.info("Database connection closed.");
            }
        } catch (SQLException e) {
            LOG.error("Error on closing connection.");
            e.printStackTrace();
        }
    }

}