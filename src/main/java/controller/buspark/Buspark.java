package controller.buspark;

import controller.Database.DB_Queries;
import model.Bus;
import model.Driver;
import model.Route;
import model.User;

import java.sql.SQLException;

public class Buspark {

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
            ex.printStackTrace();
            return "Wrong input.";
        }
    }

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
            e.printStackTrace();
            return "Wrong input.";
        }
    }

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
