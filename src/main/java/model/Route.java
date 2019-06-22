package model;

import java.util.Random;

public class Route {

    private long id;
    private String name;

    public Route(long id, String name) {
        this.id = id;
        this.name = name;
    }

    //factory method
    public static Route create() {
        Random rand = new Random();
        String name;
        long id = rand.nextInt(1000000);
        name = "busName" + rand.nextInt(1000000);
        return new Route(id, name);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
