package controller;

import controller.buspark.Buspark;
import model.Bus;
import model.Driver;
import model.Route;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnConfirmRouteAction implements ActionListener {

    private Buspark buspark;
    private ConfirmRouteAction confirmRouteAction;
    private Bus bus;
    private Route route;
    private Driver driver;

    public OnConfirmRouteAction(Buspark buspark, ConfirmRouteAction confirmRouteAction) {
        this.buspark = buspark;
        this.confirmRouteAction = confirmRouteAction;
    }

    public void getDriverBusRoute() {
        bus = buspark.getBusByDriver(driver);
        route = buspark.getRouteByBus(bus);
        driver = ((Driver) buspark.getCurrentLogedInUser());
    }

    public void confirmRoute() {
        if ((buspark.getCurrentLogedInUser() != null) && bus != null && route != null) {
            buspark.confirmRoute(driver);
            driver.setRouteConfirmed(!driver.isRouteConfirmed());
        }
    }

    public void updateRouteStatus() {
        String routeStatus;
        if (bus != null && route != null) {
            routeStatus = ("Route #" + route.getId() + " by the name " +
                    route.getName() + " is " + ((driver).isRouteConfirmed() ?
                    "" : "not ") + "confirmed. Assigned Bus #" + bus.getId() + " by the name " + bus.getName());
            // ((Driver)buspark.getCurrentLogedInUser()).setRouteConfirmed(true);111111111111111111
        } else if (route == null && bus != null) {
            routeStatus = ("Assigned Bus #" + bus.getId() + " by the name " + bus.getName());
        } else {
            routeStatus = ("No routes or buses assigned.");
        }
        confirmRouteAction.updateUIonConfirmRoute(routeStatus);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getDriverBusRoute();
        confirmRoute();
        updateRouteStatus();
    }

}
