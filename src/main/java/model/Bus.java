package model;

import java.util.Random;

public class Bus {

    private long id;
    private String name;
    private long routeId;

    public Bus(long id, String name, long routeId) {
        this.id = id;
        this.name = name;
        this.routeId = routeId;
    }

    //factory method
    public static Bus create() {
        Random rand = new Random();
        String name;
        long id = rand.nextInt(1000000), routeId = 0;
        name = "busName" + rand.nextInt(1000000);
        return new Bus(id, name, routeId);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getRouteId() {
        return routeId;
    }
}
