import controller.Database.ConnectionPool;
import controller.Database.DB_Queries;
import controller.buspark.Buspark;
import controller.exceptions.MaximumPoolSizeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test_DB_QueriesSelectAll extends DB_QueriesTest {

    @Before
    public void setup() {
        dbq = new DB_Queries(ConnectionPool.getConnectionPool());
        buspark = Buspark.getInstance(ConnectionPool.getConnectionPool());
        if (d == null) {
            try {
                dbq.addDriver(testDriverName, "testDriverPassword");
            } catch (MaximumPoolSizeException e) {
                e.printStackTrace();
            }
        }
        d = getDriverByName(testDriverName);
        if (b == null) {
            try {
                dbq.addBus(testBusName);
            } catch (MaximumPoolSizeException e) {
                e.printStackTrace();
            }
        }
        b = getBusByName(testBusName);
    }


    @Test
    public void testGetAllDrivers() {
        int selectSize = 10;
        for (int i = 0; i < selectSize; i++) {
            try {
                dbq.addDriver(testDriverName, "1111");
            } catch (MaximumPoolSizeException e) {
                e.printStackTrace();
            }
        }
        String[][] res = new String[0][];
        try {
            res = dbq.getAllDrivers(0, selectSize);
        } catch (MaximumPoolSizeException e) {
            e.printStackTrace();
        }
        assertEquals(selectSize, res.length);
        removeDriverByName(testDriverName);
    }

    @Test
    public void testGetAllBuses() {
        int selectSize = 10;
        for (int i = 0; i < selectSize; i++) {
            try {
                dbq.addBus(testBusName);
            } catch (MaximumPoolSizeException e) {
                e.printStackTrace();
            }
        }
        String[][] res = new String[0][];
        try {
            res = dbq.getAllBuses(0, selectSize);
        } catch (MaximumPoolSizeException e) {
            e.printStackTrace();
        }
        assertEquals(selectSize, res.length);
        removeBusByName(testBusName);
    }

    @Test
    public void testGetAllRoutes() {
        int selectSize = 10;
        for (int i = 0; i < selectSize; i++) {
            try {
                dbq.addRoute(testRouteName);
            } catch (MaximumPoolSizeException e) {
                e.printStackTrace();
            }
        }
        String[][] res = new String[0][];
        try {
            res = dbq.getAllRoutes(0, selectSize);
        } catch (MaximumPoolSizeException e) {
            e.printStackTrace();
        }
        assertEquals(selectSize, res.length);
        removeRouteByName(testRouteName);
    }
}
