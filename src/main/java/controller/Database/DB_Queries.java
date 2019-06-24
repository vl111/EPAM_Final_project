package controller.Database;

import controller.exceptions.MaximumPoolSizeException;
import model.*;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

/*This class contains all the queries to SQL database.
 * */

public class DB_Queries {
    private final static Logger LOG = Logger.getLogger(DB_Queries.class.getSimpleName());

    private ConnectionPool connPool;


    public DB_Queries(ConnectionPool connPool) {
        this.connPool = connPool;
    }


    public void addDriver(String name, String password) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        try {
            conn.getStatement().executeUpdate("INSERT INTO drivers(driv_name, password) " +
                    "VALUES ('" + name + "', '" + password + "');");
            conn.getConnection().commit();
        } catch (SQLException e) {
            LOG.error("SQL exception on adding new driver.");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
        }
    }

    public void addAdministrator(String name, String password) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        try {
            conn.getStatement().executeUpdate("INSERT INTO administrators(adm_name, password) " +
                    "VALUES ('" + name + "', '" + password + "');");
            conn.getConnection().commit();
            LOG.info("Administrator added");
        } catch (SQLException e) {
            LOG.error("Exception on adding administrator.");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
        }

    }

    public void addRoute(String name) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        try {
            conn.getStatement().executeUpdate("INSERT INTO routes(route_name) " +
                    "VALUES ('" + name + "');");
            conn.getConnection().commit();
            LOG.info("Route added.");
        } catch (SQLException e) {
            LOG.error("Exception on adding new route.");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
        }

    }

    public void addBus(String name) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        try {
            conn.getStatement().executeUpdate("INSERT INTO buses(bus_name) " +
                    "VALUES ('" + name + "');");
            conn.getConnection().commit();
            LOG.info("Bus added.");
        } catch (SQLException e) {
            LOG.error("Exception on adding new bus.");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
        }

    }

    public void assignDriverToBus(long driverId, long busId) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        try {
            conn.getStatement().executeUpdate("update drivers set bus_id = " + busId
                    + ", route_confirmed = false where id = " + driverId + ";");
            conn.getConnection().commit();
            LOG.info("Driver assigned to the bus.");
        } catch (SQLException e) {
            LOG.error("Exception on assigning driver to the bus.");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
        }
    }

    public void unassignDriverFromBus(long driverId) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        try {
            conn.getStatement().executeUpdate("update drivers set bus_id = null" +
                    ", route_confirmed = false where id = " + driverId + ";");
            conn.getConnection().commit();
            LOG.info("Driver unassigned from the bus.");
        } catch (SQLException e) {
            LOG.error("Error on unassigning driver from the bus.");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
        }
    }

    public void assignBusToRoute(long busId, long routeId) throws SQLException, MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        LOG.info("Creating savepoint.");
        Savepoint savepoint = conn.getConnection().setSavepoint("Savepoint");
        try {
            LOG.warn("Assigning bus to route.");
            conn.getStatement().executeUpdate("update buses set route_id = " + routeId
                    + " where id = " + busId + ";");
            conn.getStatement().executeUpdate("update drivers set " +
                    "route_confirmed = false where bus_id = " + busId + ";");
            conn.getConnection().commit();
            LOG.info("Bus assigned to the route.");
        } catch (SQLException e) {
            LOG.error("Exception on assigning Bus to the route.");
            conn.getConnection().rollback(savepoint);
            LOG.info("Rolled back to the savepoint.");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
        }
    }

    public void unassignBusFromRoute(long busId) throws SQLException, MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        Savepoint savepoint = conn.getConnection().setSavepoint("Savepoint");
        try {
            LOG.warn("Unassigning bus from route.");
            conn.getStatement().executeUpdate("update buses set route_id = null" +
                    " where id = " + busId + ";");
            conn.getStatement().executeUpdate("update drivers set " +
                    "route_confirmed = false where bus_id = " + busId + ";");
            conn.getConnection().commit();
            LOG.info("Bus unassigned from the route.");
        } catch (SQLException e) {
            LOG.error("Exception on assigning Bus from the route.");
            conn.getConnection().rollback(savepoint);
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
        }
    }

    //reverses the value of route_confirmed in driver table;
    public void confirmRoute(Driver driver) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        try {
            String toChange = (driver.isRouteConfirmed() ? "false" : "true");
            conn.getStatement().executeUpdate("update drivers set route_confirmed = " +
                    toChange + " where id = " + driver.getId() + ";");
            conn.getConnection().commit();
            LOG.info("route confirmed");
        } catch (SQLException e) {
            LOG.error("Exception on confirming route.");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);

        }
    }

    public Driver getDriverById(long id) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        Driver driver = null;
        ResultSet result;
        try {
            LOG.warn("Getting driver by id.");
            result = conn.getStatement().executeQuery("select d.driv_name, b.bus_name, b.id," +
                    " d.route_confirmed  from drivers d " +
                    " left join buses b on d.bus_id = b.id where d.id = " + id + ";");//, d.route_id not null
            conn.getConnection().commit();
            if (result.next()) {
                driver = new Driver(id, result.getString(1), result.getLong(3),
                        result.getBoolean(4));
            }
        } catch (SQLException e) {
            LOG.error("Error on getting driver by id.");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
            return driver;
        }


    }

    public Bus getBusById(long id) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        Bus bus = null;
        ResultSet result;
        try {
            LOG.warn("Getting bus by id.");
            result = conn.getStatement().executeQuery("select bus_name, route_id" +
                    " from buses where id = " + id + ";");
            conn.getConnection().commit();
            if (result.next()) {
                bus = new Bus(id, result.getString(1), result.getLong(2));
            }

        } catch (SQLException e) {
            LOG.error("Error on getting bus by id");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
            return bus;
        }

    }

    public Route getRouteById(long id) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        Route route = null;
        ResultSet result;
        try {
            LOG.warn("Getting route by id.");
            result = conn.getStatement().executeQuery("select id, route_name" +
                    " from routes where id = " + id + ";");
            conn.getConnection().commit();
            if (result.next()) {
                route = new Route(id, result.getString(2));
            }

        } catch (SQLException e) {
            LOG.error("Error on getting route by id.");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
            return route;
        }
    }


    public Route getRouteByBus(long id) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        Route route = null;
        ResultSet result;
        try {
            result = conn.getStatement().executeQuery("select route_name" +
                    " from routes where id = " + id + ";");//, d.route_id not null
            conn.getConnection().commit();
            if (result.next()) {
                route = new Route(id, result.getString(1));
            }

        } catch (SQLException e) {
            LOG.error("Error on getting route by bus");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
            return route;
        }
    }

    //Method returns logged in use in case success, and null in case fail.
    public User logInUser(long id, String pass) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        User user = null;
        ResultSet result;
        try {
            LOG.warn("Logging in user.");
            result = conn.getStatement().executeQuery("select id, driv_name, bus_id, route_confirmed from drivers  " +
                    "  where id = " + id + " AND password = '" + pass + "';");

            if (result.next()) {
                user = new Driver(id, result.getString(2), result.getLong(3),
                        result.getBoolean(4));
            } else {
                result = conn.getStatement().executeQuery("select id, adm_name from administrators  " +
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
            connPool.retrieveConnection(conn);
            return user;
        }

    }

    //Returns an array of drivers, where each row represents a driver
    // and elements in a row are driver`s params
    public String[][] getAllDrivers(int start, int offset) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        ResultSet result;
        String[][] drivers = new String[0][];
        try {
            LOG.warn("Getting drivers.");
            result = conn.getStatement().executeQuery("select d.id, d.driv_name, b.id, b.bus_name," +
                    " r.id, r.route_name, d.route_confirmed  from drivers d " +
                    " left join buses b on d.bus_id = b.id " +
                    " left join routes r on b.route_id = r.id" +
                    " LIMIT " + start + ", " + offset + ";");
            conn.getConnection().commit();

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
            connPool.retrieveConnection(conn);
            return drivers;
        }

    }

    //Returns an array of parameters of buses.
    public String[][] getAllBuses(int start, int offset) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        ResultSet result;
        String[][] buses = new String[0][];
        try {
            LOG.warn("Getting all Buses.");
            result = conn.getStatement().executeQuery("select  b.id, b.bus_name," +
                    " r.id, r.route_name from buses b " +
                    "  left join routes r on b.route_id = r.id" +
                    " LIMIT " + start + ", " + offset + ";");
            conn.getConnection().commit();

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
            connPool.retrieveConnection(conn);
            return buses;
        }

    }

    //Returns an array of parameters of routes.
    public String[][] getAllRoutes(int start, int offset) throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        ResultSet result;
        String[][] routes = new String[0][];
        try {
            LOG.warn("Getting routes");
            result = conn.getStatement().executeQuery("select  id, route_name from routes" +
                    " LIMIT " + start + ", " + offset + ";");
            conn.getConnection().commit();

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
            connPool.retrieveConnection(conn);
            return routes;
        }

    }

    //This method adds four tables to DB: drivers, administrators, buses, routes.
    public void addTables() throws MaximumPoolSizeException {
        DB_Connection conn = connPool.getConnection();
        try {
            LOG.warn("Adding tables");
            conn.getStatement().executeUpdate("create table if not exists routes(" +
                    " id integer not null primary key auto_increment," +
                    " route_name varchar(25) not null);");
            conn.getStatement().executeUpdate("create  table if not exists buses(" +
                    " id integer not null primary key auto_increment," +
                    " bus_name varchar(25) not null, " +
                    " route_id integer, FOREIGN KEY (route_id) references routes(id));");
            conn.getStatement().executeUpdate("create table if not exists drivers(" +
                    " id integer not null primary key auto_increment," +
                    " password varchar(30) not null, driv_name varchar(25) not null," +
                    " bus_id integer, route_confirmed boolean default false," +
                    " FOREIGN KEY (bus_id) references buses(id));");
            conn.getStatement().executeUpdate("create table if not exists administrators(" +
                    " id integer not null primary key auto_increment," +
                    " password varchar(30) not null, adm_name varchar(25) not null) AUTO_INCREMENT=100000000;");

            conn.getConnection().commit();
        } catch (SQLException e) {
            LOG.error("Error on adding tables.");
            e.printStackTrace();
        } finally {
            connPool.retrieveConnection(conn);
        }

    }


}