import controller.ResourceLoader;
import controller.buspark.Buspark;
import view.UserInterface;


public class Main {

    static {
    }

    {

    }

    public static void main(String[] args) {

        new ResourceLoader();
        Buspark bp = Buspark.getInstance();
        UserInterface.getInstance(bp, "name", 10);
    }

}
