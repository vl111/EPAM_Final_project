package controller.ui_logic;

import controller.buspark.Buspark;
import controller.resource_loader.Localization;
import model.Bus;
import model.Driver;
import model.Route;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*This is an action listener for the "confirm route" button in main UI.*/

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
        driver = ((Driver) buspark.getCurrentLoggedInUser());
    }

    public void confirmRoute() {
        if ((buspark.getCurrentLoggedInUser() != null) && bus != null && route != null) {
            buspark.confirmRoute(driver);
            driver.setRouteConfirmed(!driver.isRouteConfirmed());
        }
    }

    public void updateRouteStatus() {
        String routeStatus;
        if (bus != null && route != null) {
            routeStatus = ("Route #" + route.getId() + " by the name " +
                    route.getName() + " is " + ((driver).isRouteConfirmed() ?
                    "" : "not ") + "confirmed. Asassigned Bus #" + bus.getId() + " by the name " + bus.getName());
        } else if (route == null && bus != null) {
            routeStatus = ("Asassigned Bus #" + bus.getId() + " by the name " + bus.getName());
        } else {
            routeStatus = (Localization.getLocalizedValue("nothingAssigned"));
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
