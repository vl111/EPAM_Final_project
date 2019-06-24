package controller.buspark;

import controller.Database.ConnectionPool;
import controller.Database.DB_Queries;
import controller.exceptions.MaximumPoolSizeException;
import controller.resource_loader.Localization;
import model.Bus;
import model.Driver;
import model.Route;
import model.User;
import org.apache.log4j.Logger;
import view.Message;

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

    private Buspark(ConnectionPool connPool) {
        dbq = new DB_Queries(connPool);
    }

    public static Buspark getInstance(ConnectionPool connPool) {
        if (instance == null)
            synchronized (Buspark.class) {
                if (instance == null) {
                    instance = new Buspark(connPool);
                }
            }
        return instance;
    }

    public User logIn(long id, String password) {
        User user = null;
        try {
            user = dbq.logInUser(id, password);
        } catch (MaximumPoolSizeException e) {
            Message.showMessage(e.getMessage());
            e.printStackTrace();
        }
        if (user != null) {
            currentLoggedInUser = user;
        }
        return currentLoggedInUser;
    }

    public void logOut() {
        currentLoggedInUser = null;
    }

    public Driver getDriverById(long id) {
        try {
            return dbq.getDriverById(id);
        } catch (MaximumPoolSizeException e) {
            Message.showMessage(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Bus getBusById(long id) {
        try {
            return dbq.getBusById(id);
        } catch (MaximumPoolSizeException e) {
            Message.showMessage(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Route getRouteById(long id) {
        try {
            return dbq.getRouteById(id);
        } catch (MaximumPoolSizeException e) {
            Message.showMessage(e.getMessage());
            e.printStackTrace();
        }
        return null;
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
        } catch (MaximumPoolSizeException e) {
            e.printStackTrace();
            return e.getMessage();
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
        } catch (MaximumPoolSizeException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //executes confirmRoute method from the DB_Queries class
    public void confirmRoute(Driver driver) {
        if (driver != null) {
            try {
                dbq.confirmRoute(driver);
            } catch (MaximumPoolSizeException e) {
                Message.showMessage(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public String[][] getAllDriversInfo(int start, int offset) {
        try {
            return dbq.getAllDrivers(start, offset);
        } catch (MaximumPoolSizeException e) {
            Message.showMessage(e.getMessage());
            e.printStackTrace();
        }
        return new String[0][];
    }

    public String[][] getAllBusesInfo(int start, int offset) {
        try {
            return dbq.getAllBuses(start, offset);
        } catch (MaximumPoolSizeException e) {
            Message.showMessage(e.getMessage());
            e.printStackTrace();
        }
        return new String[0][];
    }

    public String[][] getAllRoutesInfo(int start, int offset) {
        try {
            return dbq.getAllRoutes(start, offset);
        } catch (MaximumPoolSizeException e) {
            Message.showMessage(e.getMessage());
            e.printStackTrace();
        }
        return new String[0][];
    }

    public Bus getBusByDriver(Driver driver) {
        if (driver != null) {
            try {
                return dbq.getBusById(driver.getBusId());
            } catch (MaximumPoolSizeException e) {
                Message.showMessage(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    public Route getRouteByBus(Bus bus) {
        if (bus != null) {
            try {
                return dbq.getRouteByBus(bus.getRouteId());
            } catch (MaximumPoolSizeException e) {
                Message.showMessage(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    public User getCurrentLoggedInUser() {
        return currentLoggedInUser;
    }

    public void setCurrentLoggedInUser(User currentLoggedInUser) {
        this.currentLoggedInUser = currentLoggedInUser;
    }
}
