package controller.factory;

import controller.Database.DB_Queries;

import java.util.Random;

public class BusFactory implements Factory {

    private DB_Queries dbq;

    public BusFactory() {
        dbq = DB_Queries.getInstance();
    }

    @Override
    public void addObjectsToDB(int numberOfObjects) {
        Random rand = new Random();
        String name, pass;
        for (int i = 0; i < numberOfObjects; i++) {
            name = "busName" + rand.nextInt(1000000);
            dbq.addBus(name);
        }
    }
}