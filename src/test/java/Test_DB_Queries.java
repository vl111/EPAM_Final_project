import controller.Database.DB_Queries;
import org.junit.Before;

public class Test_DB_Queries {

    DB_Queries dbq;

//    @Test
//    public void test

    @Before
    public void setup() {
        dbq = DB_Queries.getInstance();
        dbq.addTables();
    }


}
