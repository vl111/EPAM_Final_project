import controller.buspark.Buspark;
import controller.resource_loader.ResourceLoader;
import view.UserInterface;


public class Main {

    public static void main(String[] args) {
        new ResourceLoader();
        Buspark bp = Buspark.getInstance();
        UserInterface.getInstance(bp, "Bus park", 10);
    }

}
