package core.Filter;

import core.data.Book;
import core.data.BookList;
import core.data.User;
import core.service.BookManager;

import java.util.List;

public class ListFilter extends BookFilter{

    private User user;
    public ListFilter(String condition, User user) {
        super(condition);
        this.user = user;
    }

    @Override
    public boolean include(Book book) {

        boolean found = false;

        List<BookList> lists = BookManager.getInstance().getBookLists(user.getId() );
        for( BookList list : lists ){
            if( BookManager.getInstance().getBooks( list ).contains( book ) ){
                found = true;
            }
        }

        return found;
    }
}
