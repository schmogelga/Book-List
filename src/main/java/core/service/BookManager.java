package core.service;

import core.Filter.BookFilter;
import core.Filter.Filter;
import core.data.*;
import db.persistence.*;
import db.providers.DAOFactory;
import misc.util.Handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookManager {

    private BookListDAO bookListDAO;
    private PublisherDAO publisherDAO;
    private AuthorDAO authorDAO;
    private CategoryDAO categoryDAO;
    private BookDAO bookDAO;
    private static BookManager instance;

    private BookManager() {

        this.bookListDAO = (BookListDAO) DAOFactory.getInstance().get( BookListDAO.class );
        this.bookDAO = (BookDAO) DAOFactory.getInstance().get( BookDAO.class );
        this.publisherDAO = (PublisherDAO) DAOFactory.getInstance().get( PublisherDAO.class );
        this.authorDAO = (AuthorDAO) DAOFactory.getInstance().get( AuthorDAO.class );
        this.categoryDAO = (CategoryDAO) DAOFactory.getInstance().get( CategoryDAO.class );
    }

    public static BookManager getInstance() {

        if ( instance == null ) {

            instance = new BookManager();
        }
        return instance;
    }

    public BookList createBookList( int userId, String name, String description ) {

        BookList bookList = new BookList( name, userId );
        bookList.setDescription( description);

        return addBookList( bookList );
    }

    public BookList addBookList( BookList bookList ) {

        try {
            bookListDAO.add( bookList );
        } catch ( SQLException e ) {
            Handler.handle( e );
        }

        return bookList;
    }

    public void addToList( Book book, BookList bookList ) {

        addBook( book );

        for( Book value : getBooks( bookList ) ) {
            if( value.equals( book ) ) {
                return;
            }
        }

        bookDAO.addBookListMapping( book.getId(), bookList.getId() );
    }

    public ArrayList<Book> getBooks( BookList list ) {

        ArrayList<Book> books = new ArrayList<>();
        try {
            books = bookDAO.getBooks( list );
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return books;
    }

    public List<Author> getAuthors() {
        List<Author> authors = new ArrayList<>();
        try {
            authors = authorDAO.fetch();
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return authors;
    }

    public List<Publisher> getPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        try {
            publishers = publisherDAO.fetch();
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return publishers;
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            categories = categoryDAO.fetch();
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return categories;
    }

    public void addBook( Book book ) {

        try {
            Book dbBook = getBook( book.getISBN_10() );

            if( dbBook == null ) {
                bookDAO.add( book );

                addPublisher( book.getPublisher() );
                bookDAO.addBookPublisherMapping( book.getId(), getPublisher( book.getPublisher().getName()  ).getId() );

                for( Author author : book.getAuthors() ) {
                    addAuthor( author );
                    bookDAO.addBookAuthorMapping( book.getId(), getAuthor( author.getName() ).getId() );
                }
                for( Category category : book.getCategories() ) {
                    addCategory( category );
                    bookDAO.addBookCategoryMapping( book.getId(), getCategory( category.getName() ).getId() );
                }

                //TODO: create publisher author and cetegories mapping

            }
        } catch (SQLException e) {
            Handler.handle( e );
        }
    }

    public void addPublisher( Publisher publisher ) {

        try {
            Publisher dbPublisher = getPublisher( publisher.getName() );

            if( dbPublisher == null ) {
                publisherDAO.add( publisher );
            }
        } catch (SQLException e) {
            Handler.handle( e );
        }
    }

    public void addAuthor( Author author ) {

        try {
            Author dbAuthor = getAuthor( author.getName() );

            if( dbAuthor == null ) {
                authorDAO.add( author );
            }
        } catch (SQLException e) {
            Handler.handle( e );
        }
    }

    public void addCategory( Category category ) {

        try {
            Category dbCategory = getCategory( category.getName() );

            if( dbCategory == null ) {
                categoryDAO.add( category );
            }
        } catch (SQLException e) {
            Handler.handle( e );
        }
    }

    public Book getBook( String ISBN_10 ) {

        Book book = null;
        try {
            book = bookDAO.get( ISBN_10 );
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return book;
    }

    public BookList getBookList( int id ) {

        BookList bookList = null;
        try {
            bookList = bookListDAO.get( id );
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return bookList;
    }

    public ArrayList<BookList> getBookLists( int userId ) {

        ArrayList<BookList> bookLists = new ArrayList<>();
        try {
            bookLists = bookListDAO.getBookLists( userId );
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return bookLists;
    }

    public void updateBookList( BookList bookList ) {

        try {
            bookListDAO.update( bookList );
        } catch (SQLException e) {
            Handler.handle( e );
        }
    }

    public void deleteBookList( BookList bookList ) {

        try {
            bookListDAO.delete( bookList );
        } catch (SQLException e) {
            Handler.handle( e );
        }
    }

    public Publisher getPublisher( String name ) {

        Publisher publisher = null;
        try {
            publisher = publisherDAO.get( name );
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return publisher;
    }

    public Author getAuthor(String name ) {

        Author author = null;
        try {
            author = authorDAO.get( name );
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return author;
    }

    public Category getCategory(String name ) {

        Category category = null;
        try {
            category = categoryDAO.get( name );
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return category;
    }

    public List<Book> getBooks(User user) {

        List<Book> allUserBooks = new ArrayList<>();

        for ( BookList bookList : getBookLists( user.getId() ) ){
            allUserBooks.addAll( getBooks( bookList ) );
        }

        return allUserBooks;
    }

    public List<Publisher> getPublishers( Book book ) {

        List<Publisher> publisher = null;
        try {
            publisher = publisherDAO.get( book );
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return publisher;
    }

    public List<Author> getAuthors( Book book ) {

        List<Author> authors = null;
        try {
            authors = authorDAO.get( book );
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return authors;
    }

    public List<Category> getCategories( Book book ) {

        List<Category> categories = null;
        try {
            categories = categoryDAO.get( book );
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return categories;
    }

    public List<Book> getBooks( List<Book> books,  BookFilter filter ){

        List<Book> filteredBooks = new ArrayList<>();
        for( Book book : books ){
            if( filter.include( book ) ) {
                filteredBooks.add( book );
            }
        }

        return filteredBooks;
    }

    public List<BookList> getLists(Book book, User user) {

        List<BookList> bookLists = null;
        try {
            bookLists = bookListDAO.get( user, book );
        } catch (SQLException e) {
            Handler.handle( e );
        }

        return bookLists;

    }

    public void unmapBook(BookList list, Book book) {

        bookListDAO.deleteBookListMapping( list, book );
    }
}
