package core.Filter;

import core.data.Author;
import core.data.Book;
import core.service.BookManager;

public class AuthorFilter extends BookFilter {
    public AuthorFilter(String condition) {
        super(condition);
    }

    @Override
    public boolean include( Book book ) {

        boolean found = false;

        for( Author author : BookManager.getInstance().getAuthors( book ) ){
            if( author.getName().toLowerCase().contains( condition ) ) {
                System.out.println( book );
                found = true;
            }
        }

        return found;
    }
}
