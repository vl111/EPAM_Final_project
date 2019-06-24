import controller.Database.ConnectionPool;
import controller.Database.DB_Queries;
import controller.buspark.Buspark;
import controller.exceptions.MaximumPoolSizeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test_DB_QueriesAssignUnassignDriverBus extends DB_QueriesTest {

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


    @After
    public void after() {
        removeBusByName(testBusName);
        removeDriverByName(testDriverName);
    }


    @Test
    @Ignore
    @Order(1)
    public void testAddTables() {
        try {
            dbq.addTables();
        } catch (MaximumPoolSizeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void testAssignUnassignDriverBus() {
        buspark.assignUnassign(d, b);
        d = getDriverByName(testDriverName);
        assertEquals(true, d.getBusId() > 0);
        buspark.assignUnassign(d, b);
        d = getDriverByName(testDriverName);
        assertEquals(false, d.getBusId() > 0);
        buspark.assignUnassign(d, b);
        d = getDriverByName(testDriverName);
        assertEquals(true, d.getBusId() > 0);
    }
}