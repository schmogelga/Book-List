package core.Filter;

import core.data.Book;
import core.data.Category;
import core.service.BookManager;

public class CategoryFilter extends BookFilter {
    public CategoryFilter(String condition) {
        super(condition);
    }

    @Override
    public boolean include( Book book ) {

        boolean found = false;

        for( Category category : BookManager.getInstance().getCategories( book ) ){
            if( category.getName().toLowerCase().contains( condition ) ) {
                found = true;
            }
        }

        return found;
    }
}
