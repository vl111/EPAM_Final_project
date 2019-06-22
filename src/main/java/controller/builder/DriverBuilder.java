package controller.builder;

import model.Driver;

public class DriverBuilder extends UserBuilder {

    private long busId;
    private boolean routeConfirmed;

    public DriverBuilder() {
        super();
        busId = 0;
        routeConfirmed = false;
    }

    public DriverBuilder buildBusId(long BusId) {
        this.busId = busId;
        return this;
    }

    public DriverBuilder buildRouteConfiremd(boolean routeConfirmed) {
        this.routeConfirmed = routeConfirmed;
        return this;
    }

    Driver build() {
        Driver dr = new Driver(id, name, busId, routeConfirmed);
        return dr;
    }

}
