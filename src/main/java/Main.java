import controller.Buspark;
import view.UserInterface;


public class Main {

    static {
    }

    {

    }

    public static void main(String[] args) {

        new controller.ResourceLoader();
        Buspark bp = Buspark.getInstance();
        UserInterface.getInstance(bp, "name", 10);
    }

}
