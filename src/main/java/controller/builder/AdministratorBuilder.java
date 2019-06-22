package controller.builder;

import model.Administrator;

public class AdministratorBuilder extends UserBuilder {

    public AdministratorBuilder() {
        super();
    }

    Administrator build() {
        Administrator adm = new Administrator(id, name);
        return adm;
    }
}
