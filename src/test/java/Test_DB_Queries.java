import controller.Database.DB_Queries;
import org.junit.Before;
import org.junit.Test;

public class Test_DB_Queries {

    DB_Queries dbq;


    @Test
    public void test() {
        dbq.addTables();

    }

    @Before
    public void setup() {
        dbq = DB_Queries.getInstance();
    }
}