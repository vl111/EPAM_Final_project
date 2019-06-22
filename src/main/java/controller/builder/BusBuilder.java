package controller.builder;

import model.Bus;

public class BusBuilder {
    private long id;
    private String name;
    private long routeId;

    public BusBuilder() {
        super();
        id = 0;
        name = "busName";
        routeId = 0;
    }

    public BusBuilder buildId(long Id) {
        this.id = id;
        return this;
    }

    public BusBuilder buildBusName(String name) {
        this.name = name;
        return this;
    }

    public BusBuilder buildRouteId(long routeId) {
        this.routeId = routeId;
        return this;
    }

    Bus build() {
        Bus bus = new Bus(id, name, routeId);
        return bus;
    }
}
