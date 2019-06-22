package controller.buspark;

import controller.Database.DB_Queries;
import model.Bus;
import model.Driver;
import model.Route;
import model.User;
import org.apache.log4j.Logger;

import java.sql.SQLException;

/*This class serves as a command class, and it used as an interaction between
 the UserInterface class and the DB_Queries class.
 Most methods in this class are simply executes methods from the DB_Queries class.
* */

public class Buspark {
    private final static Logger LOG = Logger.getLogger(Buspark.class.getSimpleName());

    private static volatile Buspark instance;

    private User currentLogedInUser;
    private DB_Queries dbq;

    private Buspark() {
        dbq = DB_Queries.getInstance();
    }

    public static Buspark getInstance() {
        if (instance == null)
            synchronized (Buspark.class) {
                if (instance == null) {
                    instance = new Buspark();
                }
            }
        return instance;
    }

    public User logIn(long id, String password) {
        User user = null;
        user = dbq.logInUser(id, password);
        if (user != null) {
            currentLogedInUser = user;
        }
        return currentLogedInUser;
    }

    public void logOut() {
        currentLogedInUser = null;
    }

    public Driver getDriverById(long id) {
        return dbq.getDriverById(id);
    }

    public Bus getBusById(long id) {
        return dbq.getBusById(id);
    }

    public Route getRouteById(long id) {
        return dbq.getRouteById(id);
    }

    //sets a driver to a bus (or makes dirver free,
    // if he already signed to the bus passed in params)
    public String signUnsign(Driver dr, Bus bus) {
        try {
            if (dr.getBusId() > 0 && dr.getBusId() == bus.getId()) {
                dbq.unsignDriverFromBus(dr.getId());
                return "Usigned driver id: " + dr.getId() + " name: " + dr.getName() +
                        " from bus: " + bus.getId() + " name: " + bus.getName();
            } else {
                dbq.signDriverToBus(dr.getId(), bus.getId());
                return "Signed driver id: " + dr.getId() + " name: " + dr.getName() +
                        " to bus: " + bus.getId() + " name: " + bus.getName();
            }
        } catch (NullPointerException ex) {
            LOG.error("NullException on signing driver to a bus.");
            ex.printStackTrace();
            return "Wrong input.";
        }
    }

    //sets a bus to a route (or unsignes bus ,
    // if it already signed to the route passed in params)
    public String signUnsign(Bus bus, Route route) {
        try {
            if (bus.getRouteId() > 0 && route.getId() == bus.getRouteId()) {
                dbq.unsignBusFromRoute(bus.getId());
                return "Usigned Bus id: " + bus.getId() + " name: " + bus.getName() +
                        " from route: " + route.getId() + " name: " + route.getName();
            } else {
                dbq.signBusToRoute(bus.getId(), route.getId());
                return "Signed Bus id: " + bus.getId() + " name: " + bus.getName() +
                        " to route: " + route.getId() + " name: " + route.getName();
            }
        } catch (SQLException | NullPointerException e) {
            LOG.error("Exception on signing bus to a route");
            e.printStackTrace();
            return "Wrong input.";
        }
    }

    //executes confirmRoute method from the DB_Queries class
    public void confirmRoute(Driver driver) {
        if (driver != null) {
            dbq.confirmRoute(driver);
        }
    }

    public String[][] getAllDriversInfo(int start, int offset) {
        return dbq.getAllDrivers(start, offset);
    }

    public String[][] getAllBusesInfo(int start, int offset) {
        return dbq.getAllBuses(start, offset);

    }

    public String[][] getAllRoutesInfo(int start, int offset) {
        return dbq.getAllRoutes(start, offset);
    }

    public Bus getBusByDriver(Driver driver) {
        if (driver != null)
            return dbq.getBusById(driver.getBusId());
        else return null;
    }

    public Route getRouteByBus(Bus bus) {
        if (bus != null)
            return dbq.getRouteByBus(bus.getRouteId());
        else return null;
    }

    public User getCurrentLogedInUser() {
        return currentLogedInUser;
    }


}
