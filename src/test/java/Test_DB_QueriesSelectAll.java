import controller.Database.DB_Queries;
import controller.buspark.Buspark;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test_DB_QueriesSelectAll extends DB_QueriesTest {

    @Before
    public void setup() {
        dbq = DB_Queries.getInstance();
        buspark = Buspark.getInstance();
        if (d == null)
            dbq.addDriver(testDriverName, "testDriverPassword");
        d = getDriverByName(testDriverName);
        if (b == null)
            dbq.addBus(testBusName);
        b = getBusByName(testBusName);
    }


    @Test
    public void testGetAllDrivers() {
        int selectSize = 10;
        for (int i = 0; i < selectSize; i++) {
            dbq.addDriver(testDriverName, "1111");
        }
        String[][] res = dbq.getAllDrivers(0, selectSize);
        assertEquals(selectSize, res.length);
        removeDriverByName(testDriverName);
    }

    @Test
    public void testGetAllBuses() {
        int selectSize = 10;
        for (int i = 0; i < selectSize; i++) {
            dbq.addBus(testBusName);
        }
        String[][] res = dbq.getAllBuses(0, selectSize);
        assertEquals(selectSize, res.length);
        removeBusByName(testBusName);
    }

    @Test
    public void testGetAllRoutes() {
        int selectSize = 10;
        for (int i = 0; i < selectSize; i++) {
            dbq.addRoute(testRouteName);
        }
        String[][] res = dbq.getAllRoutes(0, selectSize);
        assertEquals(selectSize, res.length);
        removeRouteByName(testRouteName);
    }
}
