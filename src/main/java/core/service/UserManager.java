package core.service;

import core.data.BookList;
import core.data.User;
import db.providers.DAOFactory;
import db.persistence.UserDAO;
import misc.exception.DataEntryException;
import misc.util.Handler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final UserDAO userDAO;
    private static UserManager instance;
    private User loggedUser;

    private UserManager() {

        this.userDAO = (UserDAO) DAOFactory.getInstance().get( UserDAO.class );
    }

    public static UserManager getInstance() {

        if ( instance == null ) {

            instance = new UserManager();
        }
        return instance;
    }

    public User createUser( String name, String email, String login, String password ){

        User user = new User();

        user.setEmail( email );
        user.setName( name );
        user.setLogin( login );
        user.setPassword( hashPassword( password ) );

        return addUser( user );
    }

    public User addUser( User user ) {

        try {

            userDAO.add( user );
        } catch ( SQLException e ) {

            Handler.handle( e );
        }

        return user;
    }

    public User getUser( int id ) {

        User user = null;
        try {
            user = userDAO.get( id );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User getUser( String login ) {

        User user = null;
        try {
            user = userDAO.get( login );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    private String hashPassword( String password ) {

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            Handler.handle( e );
        }
        messageDigest.update(password.getBytes());
        byte[] bytes = messageDigest.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public User login( String login, String password ) throws DataEntryException {

        User user = getUser( login );
        if( user == null ) {
            throw new DataEntryException( "User not found!" );
        }

        if( !user.getPassword().equals( hashPassword( password )  ) ) {
            throw new DataEntryException( "Wrong password!" );
        }

        return user;
    }

    public void changePassword( String login, String password ){

        User user = getUser( login );
        user.setPassword( hashPassword( password ) );
        updateUser( user );
    }

    public void updateUser( User user ) {

        try {
            userDAO.update( user );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteUser( User user ) {

        List<BookList> bookLists = BookManager.getInstance().getBookLists(user.getId() );
        for( BookList bookList : bookLists ) {
            BookManager.getInstance().deleteBookList( bookList );
        }

        try {
            userDAO.delete( user );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            users = userDAO.fetch();
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return users;
    }

}
