package db.persistence;

import core.data.Author;
import core.data.Book;
import core.data.Category;
import core.data.Publisher;
import db.providers.DAOFactory;
import db.providers.DefaultDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryDAO extends DefaultDAO implements CategoryDAOI {

    public CategoryDAO(DAOFactory driverManager) {
        super(driverManager);
    }

    @Override
    public void add( Category subject ) throws SQLException {

        int lastId = getLastId( "categories" );
        if( lastId == -1 ) {
            throw new SQLException( "unable to increment user id!" );
        }

        subject.setId( lastId + 1 );
        execute( "insert into categories values(" + subject.getId() + ",'" + subject.getName() + "')" );
    }
    @Override
    public void delete(Category subject) throws SQLException {

    }

    @Override
    public ArrayList<Category> fetch() throws SQLException {
        return fetchCategories( "select * from categories", "id" );
    }

    @Override
    public void update(Category subject) throws SQLException {

    }

    @Override
    public Category get(int id) throws SQLException {
        return fetchCategory( "select * from categories where id = " + id );

    }

    public List<Category> get(Book book ) throws SQLException {
        return fetchCategories( "select * from books_categories where books_id = " + book.getId(), "categories_id" );
    }

    public Category get(String name ) throws SQLException {

        return fetchCategory( "select * from categories where name = '" + name + "'" );
    }

    private ArrayList<Category> fetchCategories(String sql, String idColumn) throws SQLException {

        ArrayList<Category> categories = new ArrayList<>();
        try {
            ResultSet resultSet = fetch( sql );
            if ( resultSet.isBeforeFirst() ) {
                while ( resultSet.next() ) {
                    categories.add( Objects.requireNonNull( get( resultSet.getInt( idColumn ) ) ) );
                }
            }
        } finally {
            connection.close();
        }

        return categories;
    }

    private Category fetchCategory( String sql ) throws SQLException{

        Category category = null;
        try {
            ResultSet resultSet = fetch( sql );

            if ( resultSet.isBeforeFirst() ) {
                while ( resultSet.next() ) {

                    category = new Category();
                    category.setId( Integer.parseInt( resultSet.getString( "id" ) ) );
                    category.setName( resultSet.getString("name") );

                }
            }
        } catch ( SQLException e  ) {
            category = null;
            throw e;

        } finally {
            connection.close();
        }

        return category;
    }
}
