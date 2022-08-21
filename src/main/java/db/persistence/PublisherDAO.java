package db.persistence;

import core.data.Book;
import core.data.Publisher;
import core.data.User;
import db.providers.DAOFactory;
import db.providers.DefaultDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PublisherDAO extends DefaultDAO implements PublisherDAOI {

    public PublisherDAO( DAOFactory driverManager ) {
        super(driverManager);
    }

    @Override
    public void add( Publisher subject ) throws SQLException {

        int lastId = getLastId( "publishers" );
        if( lastId == -1 ) {
            throw new SQLException( "unable to increment user id!" );
        }

        subject.setId( lastId + 1 );
        execute( "insert into publishers values(" + subject.getId() + ",'" + subject.getName() + "')" );
    }

    @Override
    public void delete( Publisher subject ) throws SQLException {

    }

    @Override
    public ArrayList<Publisher> fetch() throws SQLException {
        return fetchPublishers( "select * from publishers", "id" );
    }

    @Override
    public void update( Publisher subject ) throws SQLException {

    }

    @Override
    public Publisher get( int id ) throws SQLException {
        return fetchPublisher( "select * from publishers where id = " + id );

    }

    private Publisher fetchBook(String sql) throws SQLException {

        Publisher publisher = null;
        try {
            ResultSet resultSet = fetch( sql );

            if ( resultSet.isBeforeFirst() ) {
                while ( resultSet.next() ) {

                    publisher = new Publisher();
                    publisher.setId( resultSet.getInt( "id" ) );
                    publisher.setName( resultSet.getString("name") );
                }
            }
        } catch ( SQLException e  ) {
            publisher = null;
            throw e;

        } finally {
            connection.close();
        }

        return publisher;
    }

    public List<Publisher> get( Book book ) throws SQLException {
        return fetchPublishers( "select * from books_publishers where books_id = " + book.getId(), "publishers_id" );
    }

    private ArrayList<Publisher> fetchPublishers(String sql, String idColumn) throws SQLException {

        ArrayList<Publisher> publisher = new ArrayList<>();
        try {
            ResultSet resultSet = fetch( sql );
            if ( resultSet.isBeforeFirst() ) {
                while ( resultSet.next() ) {
                    publisher.add( Objects.requireNonNull( get( resultSet.getInt( idColumn ) ) ) );
                }
            }
        } finally {
            connection.close();
        }

        return publisher;

    }

    public Publisher get( String name ) throws SQLException {

        return fetchPublisher( "select * from publishers where name = '" + name + "'" );
    }

    private Publisher fetchPublisher( String sql ) throws SQLException{

        Publisher publisher = null;
        try {
            ResultSet resultSet = fetch( sql );

            if ( resultSet.isBeforeFirst() ) {
                while ( resultSet.next() ) {

                    publisher = new Publisher();
                    publisher.setId( Integer.parseInt( resultSet.getString( "id" ) ) );
                    publisher.setName( resultSet.getString("name") );

                }
            }
        } catch ( SQLException e  ) {
            publisher = null;
            throw e;

        } finally {
            connection.close();
        }

        return publisher;
    }
}
