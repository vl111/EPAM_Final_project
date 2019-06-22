package model;

import java.util.Random;

public class Driver extends User {

    private long busId;
    private boolean routeConfirmed;

    public Driver(long id, String name, long busId, boolean routeConfirmed) {
        super(id, name);
        this.busId = busId;
        this.routeConfirmed = routeConfirmed;
    }

    //factory method
    public static Driver create() {
        Random rand = new Random();
        String name;
        long id = rand.nextInt(1000000), busId = 0;
        boolean routeConfirmed = false;
        name = "busName" + rand.nextInt(1000000);
        return new Driver(id, name, busId, routeConfirmed);
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
