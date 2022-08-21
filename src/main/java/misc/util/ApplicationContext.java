package misc.util;

import core.data.User;

public class ApplicationContext {

    private User loggedUser;
    private static ApplicationContext defaultInstance;

    private ApplicationContext() {

    }

    public static ApplicationContext getInstance() {

        if( defaultInstance == null ) {
            defaultInstance = new ApplicationContext();
        }

        return defaultInstance;
    }

    public User getLoggedUser() {
        return this.loggedUser;
    }

    public  void setLoggedUser( User user ) {
        this.loggedUser = user;
    }

}

