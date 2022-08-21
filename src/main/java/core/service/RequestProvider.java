package core.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import core.data.Author;
import core.data.Book;
import core.data.Category;
import core.data.Publisher;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestProvider {

    public static RequestProvider instance;

    public static RequestProvider getInstance() {

        if( instance == null ) {
            instance = new RequestProvider();
        }

        return instance;
    }

    private RequestProvider() {
    }

    public List<Book> getBooks( JsonObject booksJson ) {

        List<Book> books = new ArrayList<>();

        for( JsonElement item :  booksJson.getAsJsonArray( "items" ) ) {
            books.add( getBook( ( (JsonObject) item ).getAsJsonObject( "volumeInfo" ) ) );
        }

        return books;
    }

    public JsonObject searchBooks( String field, String search, int startIndex ) {

        List<Book> books = new ArrayList<>();

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target( "https://www.googleapis.com" );
        Response response = target.queryParam( "q", field + ":"  + search )
                                .queryParam( "startIndex", startIndex  )
                                .path( "/books/v1/volumes" ).request().get();

       //System.out.println( target.queryParam( "q", field + ":" + "\"" + search + "\"" )
       //        .queryParam( "startIndex", startIndex  )
       //        .path( "/books/v1/volumes" ) );

        String entity = response.readEntity( String.class );
        JsonObject json = new Gson().fromJson( entity, JsonObject.class );

        return json;
    }

    private Book getBook( JsonObject volumeInfo ) {

        Book book = new Book();

        JsonElement title = volumeInfo.get( "title" );
        JsonElement publisherName = volumeInfo.get( "publisher" );
        JsonElement publishDate = volumeInfo.get( "publishedDate" );
        JsonElement description = volumeInfo.get( "description" );
        JsonElement ISBN_10 = null;
        JsonElement ISBN_13 = null;
        JsonElement thumbnail = null;
        List<Author> authors = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        JsonObject imageLinks = volumeInfo.getAsJsonObject( "imageLinks" );
        if( volumeInfo.getAsJsonObject( "imageLinks" ) != null ) {
            thumbnail = imageLinks.get( "thumbnail");
        }

        if( volumeInfo.get( "industryIdentifiers" ) != null ) {
            for( JsonElement identifier : volumeInfo.getAsJsonArray( "industryIdentifiers" ) ) {

                if( identifier.getAsJsonObject().get( "type" ).getAsString().equals( "ISBN_10" ) ) {
                    ISBN_10 = identifier.getAsJsonObject().get( "identifier" ) ;
                }
                if( identifier.getAsJsonObject().get( "type" ).getAsString().equals( "ISBN_13" ) ) {
                    ISBN_13 = identifier.getAsJsonObject().get( "identifier" ) ;
                }
            }
        }

        Publisher publisher = BookManager.getInstance().getPublisher( publisherName == null ? "N/D" : publisherName.getAsString() );
        if( publisher == null ) {
            publisher = new Publisher( publisherName == null ? "N/D" : publisherName.getAsString() );
        }

        if( volumeInfo.get( "authors" ) != null ) {
            for( JsonElement authorName : volumeInfo.getAsJsonArray( "authors" ) ) {
                Author author = BookManager.getInstance().getAuthor( authorName.getAsString() );
                if( author == null ) {
                    author = new Author( authorName.getAsString() );
                }

                authors.add( author );
            }
        }

        if( volumeInfo.get( "categories" ) != null ) {
            for( JsonElement categoryName : volumeInfo.getAsJsonArray( "categories" ) ) {
                Category category = BookManager.getInstance().getCategory( categoryName.getAsString() );
                if( category == null ) {
                    category = new Category( categoryName.getAsString() );
                }

                categories.add( category );
            }
        }


        book.setTitle( title == null ? "N/D" : title.getAsString() );
        book.setPublisher( publisher );
        book.setYear( getYear( publishDate ) );
        book.setDescription( description == null ? "N/D" : description.getAsString().replaceAll("'", "" ) );
        book.setCategories( categories );
        book.setISBN_10( ISBN_10 == null ? "N/D" : ISBN_10.getAsString() );
        book.setISBN_13( ISBN_13 == null ? "N/D" : ISBN_13.getAsString() );
        book.setAuthors( authors );
        book.setThumbnail( thumbnail == null
                            ? "https://png.pngtree.com/png-vector/20191027/ourmid/pngtree-book-cover-template-vector-realistic-illustration-isolated-on-gray-background-empty-png-image_1893997.jpg"
                            : thumbnail.getAsString().replaceAll( "\"", "" ) );

        return book;
    }

    private int getYear( JsonElement jsonDate ) {

        int year = 0;
        if( jsonDate != null ) {
            String date = jsonDate.getAsString();
            Pattern pattern = Pattern.compile(".*(\\d{4}).*");

            Matcher matcher = pattern.matcher( date );
            if(matcher.matches()) {
                year = Integer.parseInt( matcher.group(1) );
            }
        }

        return year;
    }

}
