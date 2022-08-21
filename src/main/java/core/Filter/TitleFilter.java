package core.Filter;

import core.data.Book;

public class TitleFilter extends BookFilter {

    public TitleFilter(String condition) {
        super(condition);
    }

    @Override
    public boolean include( Book book ) {
        return book.getTitle().toLowerCase().contains( condition );
    }
}
