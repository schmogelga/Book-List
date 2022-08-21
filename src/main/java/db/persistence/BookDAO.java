package db.persistence;

import core.data.Book;
import core.data.BookList;
import core.data.User;
import db.providers.DAOFactory;
import db.providers.DefaultDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class BookDAO extends DefaultDAO implements BookDAOI{

    public BookDAO( DAOFactory driverManager ) {
        super( driverManager );
    }

    @Override
    public void add( Book subject ) throws SQLException {

        int lastId = getLastId( "books" );
        if( lastId == -1 ) {
            throw new SQLException( "unable to increment user id!" );
        }

        subject.setId( lastId + 1 );
        execute( "insert into books values(" + subject.getId() + ",'" + subject.getTitle() + "'," + subject.getYear() + ",'" + subject.getDescription() + "','" + subject.getThumbnail() + "','" + subject.getISBN_13() + "','" + subject.getISBN_10() + "')" );
    }

    @Override
    public void delete( Book subject ) throws SQLException {

    }

    @Override
    public ArrayList<Book> fetch() throws SQLException {
        return null;
    }

    @Override
    public void update( Book subject ) throws SQLException {


    }

    @Override
    public Book get( int id ) throws SQLException {
        return fetchBook( "select * from books where id = " + id );
    }

    public Book get( String ISBN_10 ) throws SQLException {
        return fetchBook( "select * from books where ISBN_10 = '" + ISBN_10 + "'" );
    }

    private Book fetchBook( String sql ) throws SQLException {

        Book book = null;
        try {
            ResultSet resultSet = fetch( sql );

            if ( resultSet.isBeforeFirst() ) {
                while ( resultSet.next() ) {

                    book = new Book();
                    book.setId( resultSet.getInt( "id" ) );
                    book.setTitle( resultSet.getString("title") );
                    book.setYear( resultSet.getInt( "year" ) );
                    book.setThumbnail( resultSet.getString("thumbnail") );
                    book.setDescription( resultSet.getString("synopsis") );
                    book.setISBN_10( resultSet.getString("ISBN_10") );
                    book.setISBN_13( resultSet.getString("ISBN_13") );
                }
            }
        } catch ( SQLException e  ) {
            book = null;
            throw e;

        } finally {
            connection.close();
        }

        return book;
    }
    public ArrayList<Book> getBooks( BookList list ) throws SQLException {

        ArrayList<Book> books = new ArrayList<>();
        try {
            ResultSet resultSet = fetch( "select * from lists_books where lists_id = " + list.getId() );
            if ( resultSet.isBeforeFirst() ) {
                while ( resultSet.next() ) {
                    books.add( Objects.requireNonNull( get( resultSet.getInt( "books_id" ) ) ) );
                }
            }
        } finally {
            connection.close();
        }

        return books;
    }

    public void addBookListMapping( int bookId, int listId ) {
        execute( "insert into lists_books values( " + listId + "," + bookId + ")" );
    }
    public void addBookCategoryMapping( int bookId, int categoryId ) {
        execute( "insert into books_categories values( " + bookId + "," + categoryId + ")" );
    }
    public void addBookAuthorMapping( int bookId, int authorId ) {
        execute( "insert into books_authors values( " + bookId + "," + authorId + ")" );
    }
    public void addBookPublisherMapping( int bookId, int publisherId ) {
        execute( "insert into books_publishers values( " + bookId + "," + publisherId + ")" );
    }

}
