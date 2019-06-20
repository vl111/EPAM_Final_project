package model;

public class Bus {

    private long id;
    private String name;
    private long routeId;

    public Bus(long id, String name, long routeId) {
        this.id = id;
        this.name = name;
        this.routeId = routeId;
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
