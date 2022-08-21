package core.Filter;

import core.data.Book;
import core.data.Publisher;
import core.service.BookManager;

public class PublisherFilter extends BookFilter {
    public PublisherFilter(String condition) {
        super(condition);
    }

    @Override
    public boolean include( Book book ) {

        boolean found = false;

        for( Publisher publisher : BookManager.getInstance().getPublishers( book ) ){
            if( publisher.getName().toLowerCase().contains( condition ) ) {
                found = true;
            }
        }

        return found;
    }
}
