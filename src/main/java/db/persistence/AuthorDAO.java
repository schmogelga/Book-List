package db.persistence;

import core.data.Author;
import core.data.Book;
import core.data.Publisher;
import db.providers.DAOFactory;
import db.providers.DefaultDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthorDAO extends DefaultDAO implements AuthorDAOI {

    public AuthorDAO(DAOFactory driverManager) {
        super(driverManager);
    }

    @Override
    public void add( Author subject ) throws SQLException {

        int lastId = getLastId( "authors" );
        if( lastId == -1 ) {
            throw new SQLException( "unable to increment user id!" );
        }

        subject.setId( lastId + 1 );
        execute( "insert into authors values(" + subject.getId() + ",'" + subject.getName() + "')" );
    }

    @Override
    public void delete(Author subject) throws SQLException {

    }

    @Override
    public ArrayList<Author> fetch() throws SQLException {
        return fetchAuthors( "select * from authors", "id");
    }

    @Override
    public void update(Author subject) throws SQLException {

    }

    @Override
    public Author get(int id) throws SQLException {
        return fetchAuthor( "select * from authors where id = " + id );
    }
    public Author get(String name) throws SQLException {
        return fetchAuthor( "select * from authors where name = '" + name + "'" );
    }

    public List<Author> get( Book book ) throws SQLException {
        return fetchAuthors( "select * from books_authors where books_id = " + book.getId(), "authors_id" );
    }

    private ArrayList<Author> fetchAuthors(String sql, String idColumn) throws SQLException {

        ArrayList<Author> authors = new ArrayList<>();
        try {
            ResultSet resultSet = fetch( sql );
            if ( resultSet.isBeforeFirst() ) {
                while ( resultSet.next() ) {
                    authors.add( Objects.requireNonNull( get( resultSet.getInt( idColumn ) ) ) );
                }
            }
        } finally {
            connection.close();
        }

        return authors;
    }
    private Author fetchAuthor(String sql ) throws SQLException{

        Author author = null;
        try {
            ResultSet resultSet = fetch( sql );

            if ( resultSet.isBeforeFirst() ) {
                while ( resultSet.next() ) {

                    author = new Author();
                    author.setId( Integer.parseInt( resultSet.getString( "id" ) ) );
                    author.setName( resultSet.getString("name") );

                }
            }
        } catch ( SQLException e  ) {
            author = null;
            throw e;

        } finally {
            connection.close();
        }

        return author;
    }
}
