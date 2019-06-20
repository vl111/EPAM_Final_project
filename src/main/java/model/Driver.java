package model;

public class Driver extends User {

    private long busId;
    private boolean routeConfirmed;

    public Driver(long id, String name, long busId, boolean routeConfirmed) {
        super(id, name);
        this.busId = busId;
        this.routeConfirmed = routeConfirmed;
    }

    public long getBusId() {
        return busId;
    }

    public boolean isRouteConfirmed() {
        return routeConfirmed;
    }

    public void setRouteConfirmed(boolean routeConfirmed) {
        this.routeConfirmed = routeConfirmed;
    }
}
