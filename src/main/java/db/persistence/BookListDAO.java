package db.persistence;

import core.data.Book;
import core.data.BookList;
import core.data.User;
import db.providers.DAOFactory;
import db.providers.DefaultDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookListDAO extends DefaultDAO implements BookListDAOI{

    public BookListDAO( DAOFactory driverManager ) {
        super( driverManager );
    }

    @Override
    public void add( BookList subject ) throws SQLException {

        int lastId = getLastId( "lists" );
        if( lastId == -1 ) {
            throw new SQLException( "unable to increment user id!" );
        }

        subject.setId( lastId + 1 );
        execute( "insert into lists values( " + subject.getId() + ", '" + subject.getName() + "', '" + subject.getUserId() + "', '" + subject.getDescription() + "')" );
    }

    @Override
    public void delete( BookList subject ) throws SQLException {
        execute( "delete from lists_books where lists_id = " + subject.getId() );
        execute( "delete from lists where id = " + subject.getId() );
    }
    public void deleteBookListMapping(BookList list, Book book) {
        execute( "delete from lists_books where books_id = " + book.getId() + " and lists_id = " + list.getId() );
    }


    public ArrayList<BookList> getBookLists( int userId ) throws SQLException {
        return fetchBookLists( "select * from lists where users_id = " + userId );
    }

    public List<BookList> get(User user, Book book) throws SQLException {
        return fetchBookLists( "select * from lists l \n" +
                "join lists_books lb on l.id = lb.lists_id\n" +
                "where l.users_id = " + user.getId() + "\n" +
                "and lb.books_id = " + book.getId() );
    }

    @Override
    public ArrayList<BookList> fetch() throws SQLException {
        return fetchBookLists( "select * from lists" );
    }

    public ArrayList<BookList> fetchBookLists( String sql ) throws SQLException {

        ArrayList<BookList> bookLists = new ArrayList<>();

        try {
            ResultSet resultSet = fetch( sql );

            if ( resultSet.isBeforeFirst() ) {
                while ( resultSet.next() ) {

                    BookList bookList = null;
                    bookList = new BookList();
                    bookList.setId( Integer.parseInt( resultSet.getString( "id" ) ) );
                    bookList.setName( resultSet.getString("name") );
                    bookList.setUserId( Integer.parseInt( resultSet.getString( "users_id" ) ) );
                    bookList.setDescription( resultSet.getString("description") );

                    bookLists.add( bookList );
                }
            }
        } catch ( SQLException e  ) {
            bookLists.clear();
            throw e;

        } finally {
            connection.close();
        }

        return bookLists;

    }

    @Override
    public void update( BookList subject ) throws SQLException {

        execute(
                "update\n" +
                        "        lists\n" +
                        "set\n" +
                        "        name = '" + subject.getName() + "',\n" +
                        "        users_id = '" + subject.getUserId() + "',\n" +
                        "        description = '" + subject.getDescription() + "'\n" +
                        "where\n" +
                        "        id = " + subject.getId()
        );
    }

    @Override
    public BookList get( int id ) throws SQLException {
        return fetchBookList( "select * from lists where id = " + id );
    }

    private BookList fetchBookList( String sql ) throws SQLException {

        BookList bookList = null;
        try {
            ResultSet resultSet = fetch( sql );

            if ( resultSet.isBeforeFirst() ) {
                while ( resultSet.next() ) {

                    bookList = new BookList();
                    bookList.setId( Integer.parseInt( resultSet.getString( "id" ) ) );
                    bookList.setName( resultSet.getString("name") );
                    bookList.setUserId( Integer.parseInt( resultSet.getString( "users_id" ) ) );
                    bookList.setDescription( resultSet.getString("description") );

                }
            }
        } catch ( SQLException e  ) {
            bookList = null;
            throw e;

        } finally {
            connection.close();
        }

        return bookList;

    }


}
