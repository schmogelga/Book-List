package core.Filter;

import core.data.Book;

public class ISBNFilter extends BookFilter {
    public ISBNFilter(String condition) {
        super(condition);
    }

    @Override
    public boolean include( Book book ) {
        return book.getISBN_10().toLowerCase().contains( condition ) || book.getISBN_13().toLowerCase().contains( condition );
    }

}
