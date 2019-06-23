import controller.Database.DB_Queries;
import controller.buspark.Buspark;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test_DB_QueriesAssignUnassignDriverBus extends DB_QueriesTest {

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


    @After
    public void after() {
        removeBusByName(testBusName);
        removeDriverByName(testDriverName);
    }


    @Test
    @Ignore
    @Order(1)
    public void testAddTables() {
        dbq.addTables();
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