package controller.buspark;

import controller.Database.DB_Queries;
import controller.resource_loader.Localization;
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

    private User currentLoggedInUser;
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
            currentLoggedInUser = user;
        }
        return currentLoggedInUser;
    }

    public void logOut() {
        currentLoggedInUser = null;
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
    // if he already assigned to the bus passed in params)
    public String assignUnassign(Driver dr, Bus bus) {
        try {
            if (dr.getBusId() > 0 && dr.getBusId() == bus.getId()) {
                dbq.unassignDriverFromBus(dr.getId());
                return "Unassigned driver id: " + dr.getId() + " name: " + dr.getName() +
                        " from bus: " + bus.getId() + " name: " + bus.getName();
            } else {
                dbq.assignDriverToBus(dr.getId(), bus.getId());
                return "Assigned driver id: " + dr.getId() + " name: " + dr.getName() +
                        " to bus: " + bus.getId() + " name: " + bus.getName();
            }
        } catch (NullPointerException ex) {
            LOG.error("NullException on assigning driver to a bus.");
            ex.printStackTrace();
            return Localization.getLocalizedValue("wrongInput");
        }
    }

    //sets a bus to a route (or unassignes bus ,
    // if it already assigned to the route passed in params)
    public String assignUnassign(Bus bus, Route route) {
        try {
            if (bus.getRouteId() > 0 && route.getId() == bus.getRouteId()) {
                dbq.unassignBusFromRoute(bus.getId());
                return "Unassigned Bus id: " + bus.getId() + " name: " + bus.getName() +
                        " from route: " + route.getId() + " name: " + route.getName();
            } else {
                dbq.assignBusToRoute(bus.getId(), route.getId());
                return "Assigned Bus id: " + bus.getId() + " name: " + bus.getName() +
                        " to route: " + route.getId() + " name: " + route.getName();
            }
        } catch (SQLException | NullPointerException e) {
            LOG.error("Exception on assigning bus to a route");
            e.printStackTrace();
            return Localization.getLocalizedValue("wrongInput");
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

    public User getCurrentLoggedInUser() {
        return currentLoggedInUser;
    }


}
