package controller.factory;

import controller.Database.ConnectionPool;
import controller.Database.DB_Queries;
import controller.exceptions.MaximumPoolSizeException;

import java.util.Random;

public class BusFactory implements Factory {

    private DB_Queries dbq;

    public BusFactory() {
        dbq = new DB_Queries(ConnectionPool.getConnectionPool());
    }

    @Override
    public void addObjectsToDB(int numberOfObjects) {
        Random rand = new Random();
        String name, pass;
        for (int i = 0; i < numberOfObjects; i++) {
            name = "busName" + rand.nextInt(1000000);
            try {
                dbq.addBus(name);
            } catch (MaximumPoolSizeException e) {
                e.printStackTrace();
            }
        }
    }
}