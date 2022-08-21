package misc.util;

import core.service.UserManager;
import misc.exception.DataEntryException;

public class Validator {

    public static void validateEmail( String email ) throws DataEntryException {

        if( email.isEmpty() ) throw new DataEntryException( "Enter an email!" );
        if( !email.matches( "^(.+)@(\\S+)$" ) ) throw new DataEntryException( "invalid email format: " + email );
    }

    public static void validatePassword( String password ) throws DataEntryException {

        if( password.isEmpty() ) throw new DataEntryException( "Enter an password!" );
        if( password.length() > 20 || password.length() < 8 ) throw  new DataEntryException( "Password must have 8 to 20 characters." );

        if( !password.matches( ".*[0-9].*" ) ) throw new DataEntryException( "Password must contain at least one digit [0-9]!" );
        if( !password.matches( ".*[a-z].*" ) ) throw new DataEntryException( "Password must contain at least one lowercase Latin character [a-z]!" );
        if( !password.matches( ".*[A-Z].*" ) ) throw new DataEntryException( "Password must contain at least one uppercase Latin character [A-Z]!" );
        if( !password.matches( ".*[!@#&()–[{}]:;',?/*~$^+=<>].*" ) ) throw new DataEntryException( "Password must contain at least one special character!" );
    }

    public static void validateLogin( String login ) throws DataEntryException {

        if( login.isEmpty() ) throw new DataEntryException( "Login cannot be empty!" );
        if( login.matches( ".*[!@#&()–[{}]:;',?/*~$^+=<>].*") ) throw new DataEntryException( "Login cannot have special characters!" );
        if( UserManager.getInstance().getUser( login ) != null ) throw new DataEntryException( "Login already in use!" );
    }

    public static void validateName( String name ) throws DataEntryException {

        if( name.isEmpty() ) throw new DataEntryException( "Name cannot be empty!" );
    }
}