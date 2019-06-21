package controller.Database;

import controller.ResourceLoader;
import model.Driver;
import model.*;

import java.sql.*;

public class DB_Queries {

    private static volatile DB_Queries instance = null;

    private final String NAME = ResourceLoader.getProperties().get("DB_Name").toString();
    private final String PASSWORD = ResourceLoader.getProperties().get("DB_Password").toString();
    ;
    private final String CONNECTION_URL = ResourceLoader.getProperties().get("connectionURL").toString();

    private volatile Connection conn;
    private volatile Statement statement;

    private DB_Queries() {
        synchronized (DB_Queries.class) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");//com.mysql.jdbc.Driver
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public static DB_Queries getInstance() {
        if (instance == null)
            synchronized (DB_Queries.class) {
                if (instance == null) {
                    instance = new DB_Queries();
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
            } catch (SQLException e) {
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
            } catch (SQLException e) {
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
            } catch (SQLException e) {
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
            } catch (SQLException e) {
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
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    public void signBusToRoute(long busId, long routeId) throws SQLException {
        synchronized (instance) {
            openConnection();
            Savepoint savepoint = conn.setSavepoint("Savepoint");
            try {
                statement.executeUpdate("update buses set route_id = " + routeId
                        + " where id = " + busId + ";");
                statement.executeUpdate("update drivers set " +
                        "route_confirmed = false where bus_id = " + busId + ";");
                conn.commit();
            } catch (SQLException e) {
                conn.rollback(savepoint);
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
                statement.executeUpdate("update buses set route_id = null" +
                        " where id = " + busId + ";");
                statement.executeUpdate("update drivers set " +
                        "route_confirmed = false where bus_id = " + busId + ";");
                conn.commit();
            } catch (SQLException e) {
                conn.rollback(savepoint);
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    public void confirmRoute(Driver driver) {
        synchronized (instance) {
            openConnection();
            try {
                String toChange = (driver.isRouteConfirmed() ? "false" : "true");
                statement.executeUpdate("update drivers set route_confirmed = " +
                        toChange + " where id = " + driver.getId() + ";");
                conn.commit();
            } catch (SQLException e) {
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
                result = statement.executeQuery("select d.driv_name, b.bus_name, b.id," +
                        " d.route_confirmed  from drivers d " +
                        " left join buses b on d.bus_id = b.id where d.id = " + id + ";");//, d.route_id not null
                conn.commit();
                if (result.next()) {
                    driver = new Driver(id, result.getString(1), result.getLong(3),
                            result.getBoolean(4));
                }

            } catch (SQLException e) {
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
                result = statement.executeQuery("select bus_name, route_id" +
                        " from buses where id = " + id + ";");
                conn.commit();
                if (result.next()) {
                    bus = new Bus(id, result.getString(1), result.getLong(2));
                }

            } catch (SQLException e) {
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
                result = statement.executeQuery("select id, route_name" +
                        " from routes where id = " + id + ";");
                conn.commit();
                if (result.next()) {
                    route = new Route(id, result.getString(2));
                }

            } catch (SQLException e) {
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
                e.printStackTrace();
            } finally {
                closeConnection();
                return route;
            }
        }
    }

    public User logInUser(long id, String pass) {
        openConnection();
        User user = null;
        ResultSet result;
        synchronized (instance) {
            try {
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

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
                return user;
            }
        }

    }

    public String[][] getAllDrivers(int start, int offset) {
        openConnection();
        ResultSet result;
        String[][] drivers = new String[0][];
        synchronized (instance) {
            try {
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
                e.printStackTrace();
            } finally {
                closeConnection();
                return drivers;
            }
        }
    }

    public String[][] getAllBuses(int start, int offset) {
        openConnection();
        ResultSet result;
        String[][] buses = new String[0][];
        synchronized (instance) {
            try {
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
                e.printStackTrace();
            } finally {
                closeConnection();
                return buses;
            }
        }
    }

    public String[][] getAllRoutes(int start, int offset) {
        openConnection();
        ResultSet result;
        String[][] routes = new String[0][];
        synchronized (instance) {
            try {
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
                e.printStackTrace();
            } finally {
                closeConnection();
                return routes;
            }
        }
    }

    public void addTables() {
        synchronized (instance) {
            openConnection();
            try {
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
                    }
                }
            }
        } catch (SQLException e) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
