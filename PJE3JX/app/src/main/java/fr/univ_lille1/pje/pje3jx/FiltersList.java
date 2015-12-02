package fr.univ_lille1.pje.pje3jx;

import com.j256.ormlite.field.DatabaseField;
import java.util.ArrayList;
import java.util.List;

public class FiltersList {

    @DatabaseField(generatedId = true, columnName = "filterslist_id")
    private int filtersListId;

    private String name;

    @DatabaseField(columnName = "title_filter")
    String titleFilter;
    @DatabaseField(columnName = "author_filter")
    String authorFilter;
    @DatabaseField(columnName = "collection_filter")
    String collectionFilter;
    @DatabaseField(columnName = "genre_filter")
    String genreFilter;
    @DatabaseField(columnName = "start_year_filter")
    int startYearFilter;
    @DatabaseField(columnName = "end_year_filter")
    int endYearFilter;
    @DatabaseField(columnName = "read_filter")
    int readFilter;

    public FiltersList(){}

    public FiltersList(String n){
        name = n;
    }

    public void setTitleFilter(String titleFilter) {
        this.titleFilter = titleFilter;
    }

    public void setAuthorFilter(String authorFilter) {
        this.authorFilter = authorFilter;
    }

    public void setCollectionFilter(String collectionFilter) {
        this.collectionFilter = collectionFilter;
    }

    public void setGenreFilter(String genreFilter) {
        this.genreFilter = genreFilter;
    }

    public void setStartYearFilter(int startYearFilter) {
        this.startYearFilter = startYearFilter;
    }

    public void setEndYearFilter(int endYearFilter) {
        this.endYearFilter = endYearFilter;
    }

    public void setReadFilter(int readFilter) {
        this.readFilter = readFilter;
    }

    /**
     * Tells if a book passes the filter
     * @param b the book
     * @return true if the book validates all the criterias
     */
    public boolean isSelected(Book b) {
        if (titleFilter != null && !titleFilter.equals("") && !b.getTitle().equals(titleFilter))
            return false;
        else if (authorFilter != null && !authorFilter.equals("") && !cleanString(b.getAuthor()).equals(authorFilter))
            return false;
        else if (collectionFilter != null && !collectionFilter.equals("") && !b.getCollection().equals(collectionFilter))
            return false;
        else if (genreFilter != null && !genreFilter.equals("") && !cleanString(b.getGenre()).equals(genreFilter))
            return false;
        else if (startYearFilter != 0 && !(b.getDate() > startYearFilter))
            return false;
        else if (endYearFilter != 0 && !(b.getDate() < endYearFilter))
            return false;
        else if (readFilter != 0 && (b.isRead()==(readFilter==1)))
            return false;

        return true;
    }

    /**
     * Returns the books passing the filter from the list given as parameters
     * @param list List of books to filter
     * @return the filtered list
     */
    public List<Book> getFilteredList(List<Book> list) {
        List<Book> results = new ArrayList<>();
        for(Book b:list)
            if (isSelected(b))
                results.add(b);
        return results;
    }

    public int getId() {
        return filtersListId;
    }

    public String getListName(){
        return name;
    }

    /**
     * Clean the string by removing spaces and rewriting with lowercase.
     * @param s String to clean
     * @return the cleaned string
     */
    public String cleanString(String s) {
        if(s != null)
            return s.toLowerCase().replaceAll(" ","");
        return "";
    }
}
