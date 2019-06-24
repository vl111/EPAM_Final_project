import controller.Database.ConnectionPool;
import controller.Database.DB_Queries;
import controller.buspark.Buspark;
import controller.exceptions.MaximumPoolSizeException;
import controller.resource_loader.ResourceLoader;
import view.UserInterface;


public class Main {

    public static void main(String[] args) {
        new ResourceLoader();
        Buspark bp = Buspark.getInstance(ConnectionPool.getConnectionPool());
        try {
            new DB_Queries(ConnectionPool.getConnectionPool()).addTables();
        } catch (MaximumPoolSizeException e) {
            e.printStackTrace();
        }
        UserInterface.getInstance(bp, "Bus park", 10);
    }

}
