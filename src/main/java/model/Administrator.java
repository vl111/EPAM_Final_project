package model;

import java.util.Random;

public class Administrator extends User {

    public Administrator(long id, String name) {
        super(id, name);
    }

    //factory method
    public static Administrator create() {
        Random rand = new Random();
        String name;
        long id = rand.nextInt(1000000);
        name = "adminName" + rand.nextInt(1000000);
        return new Administrator(id, name);
    }


}
