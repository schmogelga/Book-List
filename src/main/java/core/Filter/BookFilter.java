package core.Filter;

import core.data.Book;

public abstract class BookFilter implements Filter<Book> {
    protected String condition;

    public BookFilter(String condition) {
        this.condition = condition;
    }
}
