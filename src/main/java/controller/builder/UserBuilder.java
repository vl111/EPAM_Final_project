package controller.builder;

public class UserBuilder {
    protected long id;
    protected String name;

    protected UserBuilder() {
        id = 0;
        name = "userName";
    }

    public UserBuilder buildId(long id) {
        this.id = id;
        return this;
    }

    public UserBuilder buildName(String name) {
        this.name = name;
        return this;
    }
}
